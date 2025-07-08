package com.mirrormetech.corm.common.server;

import cn.hutool.core.collection.CollUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.mqtt.MqttMessageType.PUBACK;
import static io.netty.handler.codec.mqtt.MqttMessageType.PUBREC;
import static io.netty.handler.codec.mqtt.MqttQoS.*;

/**
 * @author: zhouwenjie
 * @description: 对接收到的消息进行业务处理
 * @create: 2023-04-07 16:29
 * CONNECT	    1	    C->S	客户端请求与服务端建立连接 （服务端接收）
 * CONNACK	    2	    S->C	服务端确认连接建立（客户端接收）
 * PUBLISH	    3	    C<->S	    发布消息 （服务端接收【QoS(服务质量等级) 0级别，最多分发一次】）-->生产者只会发送一次消息，不关心消息是否被代理服务端或消费者收到
 * PUBACK	    4	    C<->S	    收到发布消息确认（客户端接收【QoS 1级别，至少分发一次】） -->保证消息发送到服务端（也就是代理服务器broker），如果没收到或一定时间没收到服务端的ack，就会重发消息
 * PUBREC	    5	    C<->S	    收到发布消息（客户端接收【QoS 2级别】）|
 * PUBREL	    6	    C<->S	    释放发布消息（服务端接收【QoS 2级别】）|只分发一次消息，且保证到达 -->这三步保证消息有且仅有一次传递给消费者
 * PUBCOMP	    7	    C<->S	    完成发布消息（客户端接收【QoS 2级别】）|
 * SUBSCRIBE	8	    C->S	订阅请求（服务端接收）
 * SUBACK	    9	    S->C	订阅确认（客户端接收）
 * UNSUBSCRIBE	10	    C->S	取消订阅（服务端接收）
 * UNSUBACK	    11	    S->C	取消订阅确认（客户端接收）
 * PINGREQ	    12	    C->S	客户端发送PING(连接保活)命令（服务端接收）
 * PINGRESP	    13	    S->C	PING命令回复（客户端接收）
 * DISCONNECT	14	    C->S	断开连接 （服务端接收）
 * <p>
 * 注意：在我们发送消息的时候，一定要确认好等级，回复确认的消息统一设置为qos=0;
 * 比如，我们需要发送最高等级的消息就将qos设置为2，当我们接收方收到这个等级的消息的时候判断一下等级，设置好消息类型，然后qos因为是回复消息，
 * 所以全部设置成0，而当发送端收到消息之后，比如PUBREC，那么我们还将发送（注意不是回复）PUBREL给接收端，希望对方回复确认一下，所以qos设置为1。
 * 综上可知：发送端没有回复确认消息之说，只有发送消息，接收端没有发送消息之说，只有回复确认消息，搞清楚这个概念，在设置参数的时候就明了。
 **/

@Slf4j
@Component
public class MqttMsgBack {

    @Value("${driver.mqtt.username}")
    private String userName;

    @Value("${driver.mqtt.password}")
    private String password;

    @Value("${driver.mqtt.waitTime}")
    private long waitTime = 1000;

    /*
     * TODO 考虑将本地缓存迁移至 redis
     */
    /*private final RobotDeviceService robotDeviceService;*/

    /**
     * 功能描述:存放主题和其订阅的客户端集合
     */
    public static final ConcurrentHashMap<String, HashSet<String>> subMap = new ConcurrentHashMap<String, HashSet<String>>();

    /**
     * 功能描述:存放订阅是的服务质量等级，只有发送小于或等于这个服务质量的消息给订阅者
     */
    public static final ConcurrentHashMap<String, MqttQoS> qoSMap = new ConcurrentHashMap<String, MqttQoS>();

    /**
     * 功能描述:存放客户端和其所订阅的主题集合，用来在客户端断开的时候删除订阅中的客户端
     */
    public static final ConcurrentHashMap<String, Set<String>> clientTopicMap = new ConcurrentHashMap<String, Set<String>>();

    /**
     * 功能描述:存放需要缓存的消息，一边发送给新订阅的客户端
     */
    public static final ConcurrentHashMap<String, MqttPublishMessage> cacheRetainedMessages = new ConcurrentHashMap<String, MqttPublishMessage>();

    /**
     * 功能描述:缓存需要重复发送的消息，以便在收到ack的时候将消息内存释放掉
     */
    public static final ConcurrentHashMap<String, ByteBuf> cacheRepeatMessages = new ConcurrentHashMap<String, ByteBuf>();

    public ConcurrentHashMap<String, HashSet<String>> getSubMap(){
        return subMap;
    }

    public ConcurrentHashMap<String, Set<String>> getClientTopicMap(){
        return clientTopicMap;
    }

   /* public MqttMsgBack(RobotDeviceService robotDeviceService) {
        this.robotDeviceService = robotDeviceService;
    }*/

    /**
     * 确认连接请求
     *
     * @param ctx
     * @param mqttMessage
     */
    public void connectionAck(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttConnectMessage mqttConnectMessage;
        try {
            mqttConnectMessage = (MqttConnectMessage) mqttMessage;
            //获取连接者的ClientId
            String clientIdentifier = mqttConnectMessage.payload().clientIdentifier();
            //查询用户名密码是否正确
            String userNameNow = mqttConnectMessage.payload().userName();
            String passwordNow = mqttConnectMessage.payload().password();
            //暂时跳过密码校验
            if (true) {
                MqttFixedHeader mqttFixedHeaderInfo = mqttConnectMessage.fixedHeader();
                MqttConnectVariableHeader mqttConnectVariableHeaderInfo = mqttConnectMessage.variableHeader();
                //构建返回报文， 可变报头
                MqttConnAckVariableHeader mqttConnAckVariableHeaderBack = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, mqttConnectVariableHeaderInfo.isCleanSession());
                //构建返回报文， 固定报头 至多一次(至少—次,只有一次)
                MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.CONNACK, mqttFixedHeaderInfo.isDup(), AT_MOST_ONCE, mqttFixedHeaderInfo.isRetain(), 0x02);
                //构建连接回复消息体
                MqttConnAckMessage connAck = new MqttConnAckMessage(mqttFixedHeaderBack, mqttConnAckVariableHeaderBack);
                ctx.writeAndFlush(connAck);
                //设置节点名
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                log.info("终端登录成功,ID号:{},IP信息:{},终端号:{}", clientIdentifier, address.getHostString(), address.getPort());
                //连接后记录设备的在线状态
                /*if(StringUtils.isNullOrEmpty(clientIdentifier)){
                    MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE, false, 0x02);
                    MqttConnAckVariableHeader variableHeader =
                            new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_CLIENT_IDENTIFIER_NOT_VALID, false);
                    MqttConnAckMessage mqttConnAckMessage = new MqttConnAckMessage(fixedHeader, variableHeader);
                    ctx.writeAndFlush(mqttConnAckMessage);
                    ctx.close();
                    log.error("连接失败：" + MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD.name());
                }
                DeviceRecord deviceRecord = new DeviceRecord(clientIdentifier, DeviceStatus.ONLINE);
                boolean robotExists = robotDeviceService.robotExists(deviceRecord);
                if (robotExists) {
                    robotDeviceService.transitionStatus(deviceRecord);
                }else {
                    //设备不存在 返回  未指定错误
                    MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE, false, 0x02);
                    MqttConnAckVariableHeader variableHeader =
                            new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_UNSPECIFIED_ERROR, false);
                    MqttConnAckMessage mqttConnAckMessage = new MqttConnAckMessage(fixedHeader, variableHeader);
                    ctx.writeAndFlush(mqttConnAckMessage);
                    ctx.close();
                    log.error("连接失败：" + MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD.name());
                }*/
            } else {
                //如果用户名密码错误则提示对方
                MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE, false, 0x02);
                MqttConnAckVariableHeader variableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, false);
                MqttConnAckMessage mqttConnAckMessage = new MqttConnAckMessage(fixedHeader, variableHeader);
                ctx.writeAndFlush(mqttConnAckMessage);
                ctx.close();
                log.error("连接失败：" + MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD.name());
            }
        } catch (ClassCastException e) {
            //转换失败，对方发送的协议版本不兼容
            MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE, false, 0x02);
            MqttConnAckVariableHeader variableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION, false);
            MqttConnAckMessage mqttConnAckMessage = new MqttConnAckMessage(fixedHeader, variableHeader);
            ctx.writeAndFlush(mqttConnAckMessage);
            ctx.close();
            e.printStackTrace();
            log.error("连接失败：" + MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION.name());
        }
    }

    /**
     * 用来描述设备断开连接场景
     * 更新数据库状态
     * TODO 后续是否涉及缓存
     */
    public void disConnect(ChannelHandlerContext ctx, MqttMessage mqttMessage){
        //TODO 断开连接会显示类型转换失败 此处进行注释 直接关闭连接
        /*MqttConnectMessage mqttConnectMessage = (MqttConnectMessage) mqttMessage;
        String clientIdentifier = mqttConnectMessage.payload().clientIdentifier();*/
        /*robotDeviceService.transitionStatus(new DeviceRecord(clientIdentifier,DeviceStatus.OFFLINE));*/
        ctx.close();
    }

    /**
     * 根据qos发布确认
     * 1：表示发送的消息需要一直持久保存（不受服务器重启影响），不但要发送给当前的订阅者，并且以后新来的订阅了此Topic name的订阅者会马上得到推送。
     * 0：仅仅为当前订阅者推送此消息。
     * 假如服务器收到一个空消息体(zero-length payload)、RETAIN = 1、那么就代表需要将这个缓存消息删除掉，不再继续推送给新订阅者，前提是一定保证空消息体。
     * 两个条件需要对应上才能删除，消息体为空、主题名称对应.
     * 注意：对于每个主题，只能保留一条消息。当发布一个带有RETAIN标志的新消息时，它将替换上一条保留的消息，因此在同一主题下只会存在一条保留消息。
     *
     * @param ctx
     * @param mqttMessage 拷贝的数据需要释放，其他的消息，比如连接消息，不用转发，会在handler中自动释放
     */
    public void publishAck(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) mqttMessage;
        MqttFixedHeader mqttFixedHeaderInfo = mqttPublishMessage.fixedHeader();
        MqttQoS qos = mqttFixedHeaderInfo.qosLevel();
        //得到主题
        MqttPublishVariableHeader variableHeader = mqttPublishMessage.variableHeader();
        String topicName = variableHeader.topicName();
        //将消息发送给订阅的客户端
        ByteBuf byteBuf = mqttPublishMessage.payload();
        log.info("发布的消息为:{}",byteBuf.toString(StandardCharsets.UTF_8));
        HashSet<String> set = subMap.get(topicName);
        /*
         * 给当前topic的所有订阅者pub msg
         * 如果连接上下文或者连接本身处于非正常状态  将其从订阅列表中删除
         */
        if (set != null) {
            for (String channelId : set) {
                ChannelHandlerContext context = ServerMqttHandler.clientMap.get(channelId);
                if (context != null && context.channel().isActive()) {
                    MqttQoS cacheQos = qoSMap.get(topicName + "-" + channelId);
                    if (cacheQos != null && qos.value() <= cacheQos.value()) {
                        // retainedDuplicate()增加引用计数器，不至于后续操作byteBuf出现错误，引用计数器为0的情况，这里会清除retainedDuplicate的操作有：
                        // SimpleChannelInboundHandler处理器、编码器 MqttEncoder、和最后确认的时候释放，所以每次操作消息之前，先进行一次retainedDuplicate
                        byteBuf.retainedDuplicate();
                        context.writeAndFlush(mqttPublishMessage);
                        if (qos == AT_LEAST_ONCE || qos == EXACTLY_ONCE) {

                            //只发送服务质量等级小于等于订阅时客户端指定的服务质量等级
                            //创建一个新的缓冲区
                            byteBuf.retainedDuplicate();
                            //防止内存溢出，最后在消息被ack或者客户端断开掉线的时候，拿到并进行释放
                            cacheRepeatMessages.put(channelId, byteBuf);
                            //cachePublishMsg(qos, byteBuf, variableHeader, mqttFixedHeaderInfo, context);
                        }
                    }
                } else {
                    if (context != null) {
                        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                        log.error(address.getHostString() + "转发订阅消息提醒：客户端连接异常~");
                    }
                    //防止客户端频繁上下线导致id变化，带来不必要的空指针
                    ServerMqttHandler.clientMap.remove(channelId);
                    //删除订阅主题
                    Set<String> topicSet = clientTopicMap.get(channelId);
                    if (CollUtil.isNotEmpty(topicSet)) {
                        for (String topic : topicSet) {
                            if (!subMap.isEmpty()) {
                                HashSet<String> ids = subMap.get(topic);
                                if (CollUtil.isNotEmpty(ids)) {
                                    ids.remove(channelId);
                                    if (CollUtil.isEmpty(ids)) {
                                        subMap.remove(topic);
                                    }
                                }
                            }
                            if (!qoSMap.isEmpty()) {
                                qoSMap.remove(topic + "-" + channelId);
                            }
                        }
                    }
                    clientTopicMap.remove(channelId);
                }
            }
        }
        // 缓存消息给后订阅的客户端
        boolean retain = mqttFixedHeaderInfo.isRetain();
        if (retain) {
            if (byteBuf.readableBytes() > 0) {
                byteBuf.retainedDuplicate();
                cacheRetainedMessages.put(topicName, mqttPublishMessage);
            } else {
                MqttPublishMessage message = cacheRetainedMessages.get(topicName);
                if (message != null) {
                    cacheRetainedMessages.remove(topicName);
                    // 这里需要手动删除ByteBuf缓存,因为一直会有一份缓存在内存中备用
                    boolean release = ReferenceCountUtil.release(message);
                    log.info("缓存消息给后订阅的客户端释放成功失败：{}", release);
                }
            }
        }
        //返回消息给发送端
        switch (qos) {
            //至多一次
            case AT_MOST_ONCE:
                break;
            //至少一次
            case AT_LEAST_ONCE:
                //构建返回报文， 固定报头
                MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(PUBACK, mqttFixedHeaderInfo.isDup(), AT_MOST_ONCE, mqttFixedHeaderInfo.isRetain(), 0x02);
                //构建返回报文， 可变报头
                MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(mqttPublishMessage.variableHeader().packetId());
                //构建PUBACK消息体
                MqttPubAckMessage pubAck = new MqttPubAckMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeaderBack);
                ctx.writeAndFlush(pubAck);
                break;
            //刚好一次
            case EXACTLY_ONCE:
                //构建返回报文，固定报头
                MqttFixedHeader mfh = new MqttFixedHeader(PUBREC, false, AT_MOST_ONCE, false, 0x02);
                //构建返回报文，可变报头
                MqttPubReplyMessageVariableHeader mqttPubReplyMessageVariableHeader = new MqttPubReplyMessageVariableHeader(mqttPublishMessage.variableHeader().packetId(), MqttPubReplyMessageVariableHeader.REASON_CODE_OK, MqttProperties.NO_PROPERTIES);
                //构建PUBACK消息体
                MqttMessage mqttMessageBack = new MqttMessage(mfh, mqttPubReplyMessageVariableHeader);
                ctx.writeAndFlush(mqttMessageBack);
                break;
            default:
                break;
        }
    }

    private void cachePublishMsg(MqttQoS qos, ByteBuf byteBuf, MqttPublishVariableHeader variableHeader, MqttFixedHeader mqttFixedHeaderInfo, ChannelHandlerContext context) {
        //缓存一份消息，规定时间内没有收到ack，用作重发，重发时将isDup设置为true,代表重复消息
        MqttFixedHeader fixedHeader =
                new MqttFixedHeader(MqttMessageType.PUBLISH, true, qos, false, mqttFixedHeaderInfo.remainingLength());
        MqttPublishMessage cachePubMessage = new MqttPublishMessage(fixedHeader, variableHeader, byteBuf);
        ScheduledFuture<?> scheduledFuture = TimerData.scheduledThreadPoolExecutor.scheduleAtFixedRate
                (new MonitorMsgTime(variableHeader.packetId(), cachePubMessage, context), waitTime, waitTime, TimeUnit.MILLISECONDS);
        TimerData.scheduledFutureMap.put(variableHeader.packetId(), scheduledFuture);
    }

    /**
     * 发布完成 qos2
     *
     * @param ctx
     * @param mqttMessage
     */
    public void publishComp(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
        //构建返回报文， 固定报头
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, AT_MOST_ONCE, false, 0x02);
        //构建返回报文， 可变报头
        MqttPubReplyMessageVariableHeader mqttPubReplyMessageVariableHeader = new MqttPubReplyMessageVariableHeader(messageIdVariableHeader.messageId(), MqttPubReplyMessageVariableHeader.REASON_CODE_OK, MqttProperties.NO_PROPERTIES);
        MqttMessage mqttMessageBack = new MqttMessage(mqttFixedHeaderBack, mqttPubReplyMessageVariableHeader);
        ctx.writeAndFlush(mqttMessageBack);
    }

    /**
     * 订阅确认
     * 订阅和取消订阅没有qos2级别，默认就是1级别
     * 需要存储订阅主题和客户端、客户端和订阅主题、验证防止重复订阅、发送缓存消息
     *
     * @param ctx
     * @param mqttMessage
     */
    public void subscribeAck(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttSubscribeMessage mqttSubscribeMessage = (MqttSubscribeMessage) mqttMessage;
        MqttMessageIdVariableHeader messageIdVariableHeader = mqttSubscribeMessage.variableHeader();
        //构建返回报文， 可变报头
        MqttMessageIdVariableHeader variableHeaderBack = MqttMessageIdVariableHeader.from(messageIdVariableHeader.messageId());
        MqttSubscribePayload subscribePayload = mqttSubscribeMessage.payload();
        List<MqttTopicSubscription> mqttTopicSubscriptions = subscribePayload.topicSubscriptions();
        List<Integer> grantedQoSLevels = new ArrayList<Integer>();
        String id = ctx.channel().id().toString();
        //存储客户端订阅的主题集合，断开或者异常连接时，删除订阅ctMap和subMap里的值
        Set<String> topicSet = clientTopicMap.get(id);
        if (topicSet == null) {
            topicSet = new HashSet<String>();
        }
        for (MqttTopicSubscription subscription : mqttTopicSubscriptions) {
            String topicName = subscription.topicName();
            HashSet<String> contexts = subMap.get(topicName);
            if (contexts == null) {
                contexts = new HashSet<String>();
            }
            //先判断主题是否已经订阅过了，防止重复订阅
            boolean isSub = contexts.contains(topicName);
            if (!isSub) {
                MqttQoS qos = subscription.option().qos();
                //存储主题被订阅的客户端集合
                contexts.add(id);
                /*
                这里的Client id 来源与channel.id 所以理论上不同设备的conn不会产生一样的client id
                TODO 对同名 id 重复订阅进行处理
                TODO 开放接口，查询订阅列表信息
                 */
                qoSMap.put(topicName + "-" + id, qos);
                subMap.put(topicName, contexts);
                //存储客户端订阅的主题集合
                topicSet.add(topicName);
            }
            //存储客户端订阅的主题集合
            int value = subscription.qualityOfService().value();
            grantedQoSLevels.add(value);
        }
        for (String topic : topicSet) {
            MqttQoS cacheQos = qoSMap.get(topic + "-" + id);
            //查看订阅的主题是否需要需要发送消息
            MqttPublishMessage mqttMsg = cacheRetainedMessages.get(topic);
            if (mqttMsg != null) {
                MqttFixedHeader mqttFixedHeaderInfo = mqttMsg.fixedHeader();
                MqttQoS qos = mqttFixedHeaderInfo.qosLevel();
                if (cacheQos != null && qos.value() <= cacheQos.value()) {
                    if (mqttMsg != null) {
                        MqttPublishVariableHeader variableHeader = mqttMsg.variableHeader();
                        ByteBuf payload = mqttMsg.payload();
                        //引用计数器增加
                        payload.retainedDuplicate();
                        ctx.writeAndFlush(mqttMsg);
                        // 开启消息重发机制
                        if (qos == AT_LEAST_ONCE || qos == EXACTLY_ONCE) {
                            //引用计数器增加
                            payload.retainedDuplicate();
                            cacheRepeatMessages.put(id, payload);
                            //(qos, payload, variableHeader, mqttFixedHeaderInfo, ctx);
                        }
                    }
                }
            }
        }
        //	构建返回报文	有效负载
        MqttSubAckPayload payloadBack = new MqttSubAckPayload(grantedQoSLevels);
        //	构建返回报文	固定报头
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.SUBACK, false, AT_MOST_ONCE, false, 0);
        //	构建返回报文	订阅确认
        MqttSubAckMessage subAck = new MqttSubAckMessage(mqttFixedHeaderBack, variableHeaderBack, payloadBack);
        ctx.writeAndFlush(subAck);
    }

    /**
     * 取消订阅确认
     *
     * @param ctx
     * @param mqttMessage
     */
    public void unsubscribeAck(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttUnsubscribeMessage mqttUnsubscribeMessage = (MqttUnsubscribeMessage) mqttMessage;
        MqttMessageIdVariableHeader messageIdVariableHeader = mqttUnsubscribeMessage.variableHeader();
        //	构建返回报文	可变报头
        MqttMessageIdVariableHeader variableHeaderBack = MqttMessageIdVariableHeader.from(messageIdVariableHeader.messageId());
        //	构建返回报文	固定报头
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, AT_MOST_ONCE, false, 0x02);
        //	构建返回报文	取消订阅确认
        MqttUnsubAckMessage unSubAck = new MqttUnsubAckMessage(mqttFixedHeaderBack, variableHeaderBack);
        log.info("取消订阅回复:{}", unSubAck);
        //删除本地订阅客户端
        String id = ctx.channel().id().toString();
        List<String> topics = mqttUnsubscribeMessage.payload().topics();
        Set<String> topicSet = clientTopicMap.get(id);
        for (String topic : topics) {
            if (subMap != null) {
                HashSet<String> ids = subMap.get(topic);
                if (CollUtil.isNotEmpty(ids)) {
                    ids.remove(id);
                    if (CollUtil.isEmpty(ids)) {
                        subMap.remove(topic);
                    }
                }
            }
            if (qoSMap != null) {
                qoSMap.remove(topic + "-" + id);
            }

            if (CollUtil.isNotEmpty(topicSet)) {
                topicSet.remove(topic);
            }
        }
        ctx.writeAndFlush(unSubAck);
    }

    /**
     * 心跳响应
     *
     * @param ctx
     * @param mqttMessage
     */
    public void pingResp(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, AT_MOST_ONCE, false, 0);
        MqttMessage mqttMessageBack = new MqttMessage(fixedHeader);
        log.info("心跳回复:{}", mqttMessageBack.toString());
        ctx.writeAndFlush(mqttMessageBack);
    }

    /**
     * ------------------------------------------------------服务端作为发送消息端可能会接收的事件----------------------------------------------------------------
     * <p>
     * 收到接收方消息确认，qos>1的情况，应该删除消息缓存(缓存消息保存到线程定时中了，循环发送取消，消息缓存也没有了)，取消消息重发机制
     *
     * @param ctx
     * @param mqttMessage
     */
    public void receivePubAck(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttFixedHeader fixedHeader = mqttMessage.fixedHeader();
        MqttMessageType messageType = fixedHeader.messageType();
        if (messageType == PUBACK) {
            MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
            int messageId = variableHeader.messageId();
            //等级为1的情况，直接删除原始消息，取消消息重发机制
            ScheduledFuture<?> scheduledFuture = TimerData.scheduledFutureMap.remove(messageId);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            //移除消息记录
            ByteBuf byteBuf = cacheRepeatMessages.remove(ctx.channel().id().toString());
            if (byteBuf != null) {
                // 释放内存
                byteBuf.release();
            }
        }
        if (messageType == PUBREC) {
            //等级为2的情况，收到PUBREC报文消息，先停止消息重发机制，再响应一个PUBREL报文并且构建消息重发机制
            MqttPubReplyMessageVariableHeader variableHeader = (MqttPubReplyMessageVariableHeader) mqttMessage.variableHeader();
            int messageId = variableHeader.messageId();
            //构建返回报文，固定报头
            MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBREL, false, AT_LEAST_ONCE, false, 0);
            //构建返回报文，可变报头
            MqttPubReplyMessageVariableHeader mqttPubReplyMessageVariableHeader = new MqttPubReplyMessageVariableHeader(messageId, MqttPubReplyMessageVariableHeader.REASON_CODE_OK, MqttProperties.NO_PROPERTIES);
            MqttMessage mqttMessageBack = new MqttMessage(mqttFixedHeaderBack, mqttPubReplyMessageVariableHeader);
            //删除初始消息重发机制
            ScheduledFuture<?> scheduledFuture = TimerData.scheduledFutureMap.remove(messageId);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            //释放消息缓存
            ByteBuf byteBuf = cacheRepeatMessages.remove(ctx.channel().id().toString());
            if (byteBuf != null) {
                byteBuf.release();
            }
            ctx.writeAndFlush(mqttMessageBack);
            //重发机制要放在最下方，否则，一旦出错，会多次出发此机制
            cachePubrelMsg(messageId, ctx);
        }
    }

    private void cachePubrelMsg(int messageId, ChannelHandlerContext context) {
        //缓存一份消息，规定时间内没有收到ack，用作重发，重发时将isDup设置为true,代表重复消息
        //构建返回报文，固定报头
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBREL, true, AT_LEAST_ONCE, false, 0);
        //构建返回报文，可变报头
        MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(messageId);
        MqttMessage mqttMessageBack = new MqttMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeaderBack);
        ScheduledFuture<?> scheduledFuture = TimerData.scheduledThreadPoolExecutor.scheduleAtFixedRate(new MonitorMsgTime(messageId, mqttMessageBack, context), waitTime, waitTime, TimeUnit.MILLISECONDS);
        TimerData.scheduledFutureMap.put(messageId, scheduledFuture);
    }

    /**
     * 功能描述: 接收到最后一次确认，取消上次PUBREL的消息重发机制
     *
     * @param ctx
     * @param mqttMessage
     * @return void
     * @author zhouwenjie
     * @date 2023/6/9 16:00
     */
    public void receivePubcomp(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        MqttPubReplyMessageVariableHeader variableHeader = (MqttPubReplyMessageVariableHeader) mqttMessage.variableHeader();
        int messageId = variableHeader.messageId();
        ScheduledFuture<?> scheduledFuture = TimerData.scheduledFutureMap.remove(messageId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        ByteBuf byteBuf = cacheRepeatMessages.remove(ctx.channel().id().toString());
        if (byteBuf != null) {
            byteBuf.release();
        }
    }
}


