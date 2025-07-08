package com.mirrormetech.corm.common.server;

import cn.hutool.core.collection.CollUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerMqttHandler extends SimpleChannelInboundHandler<MqttMessage> {

    /**
     * ctx.channel().id
     * client id 和 其 连接上下文的映射
     */
    public static final ConcurrentHashMap<String, ChannelHandlerContext> clientMap = new ConcurrentHashMap<String, ChannelHandlerContext>();

    /**
     * 合法客户端的ip
     * 后续修改为从redis中获取
     */
    @Value("${driver.mqtt.address_list}")
    private List<String> addressList;

    @Autowired
    private MqttMsgBack mqttMsgBack;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        //判断连接是否合法
        /*
         * TODO 合法性校验方式待定
         *  1.出厂时在设备表中记录固定设备ip 进行校验
         */
        //if (addressList.contains(address.getHostString())) {
        if (true){
            clientMap.put(ctx.channel().id().toString(), ctx);
            super.handlerAdded(ctx);
        } else {
            ctx.close();
        }
    }

    /**
     * 功能描述: 客户端终止连接服务器会触发此函数
     *
     * @param ctx
     * @return void
     * @author zhouwenjie
     * @date 2020/4/3 16:47
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        delSubCache(ctx);
        super.channelInactive(ctx);
    }

    /**
     * 功能描述: 有客户端发消息会触发此函数
     *
     * @param ctx
     * @param mqttMessage
     * @return void
     * @author zhouwenjie
     * @date 2020/4/3 16:48
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        if (null != mqttMessage) {
            log.info("MQTT服务器接收到来自{}的消息：{}",
                    ((InetSocketAddress)ctx.channel().remoteAddress()).getHostString(), mqttMessage);
            MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
            switch (mqttFixedHeader.messageType()) {
                // ----------------------接收消息端（服务端）可能会触发的事件----------------------------------------------------------------
                case CONNECT:
                    log.info("CONNECT");
                    //	在一个网络连接上，客户端只能发送一次CONNECT报文。服务端必须将客户端发送的第二个CONNECT报文当作协议违规处理并断开客户端的连接
                    //	建议connect消息单独处理，用来对客户端进行认证管理等 这里直接返回一个CONNACK消息
                    mqttMsgBack.connectionAck(ctx, mqttMessage);
                    break;
                case PUBLISH:
                    log.info("PUBLISH");
                    //	收到消息，返回确认，PUBACK报文是对QoS 1等级的PUBLISH报文的响应,PUBREC报文是对PUBLISH报文的响应
                    mqttMsgBack.publishAck(ctx, mqttMessage);
                    break;
                case PUBREL:
                    log.info("PUBREL");
                    //	释放消息，PUBREL报文是对QoS 2等级的PUBREC报文的响应,此时我们应该回应一个PUBCOMP报文
                    mqttMsgBack.publishComp(ctx, mqttMessage);
                    break;
                case SUBSCRIBE:
                    log.info("SUBSCRIBE");
                    //	客户端订阅主题
                    //	客户端向服务端发送SUBSCRIBE报文用于创建一个或多个订阅，每个订阅注册客户端关心的一个或多个主题。
                    //	为了将应用消息转发给与那些订阅匹配的主题，服务端发送PUBLISH报文给客户端。
                    //	SUBSCRIBE报文也（为每个订阅）指定了最大的QoS等级，服务端根据这个发送应用消息给客户端
                    mqttMsgBack.subscribeAck(ctx, mqttMessage);
                    break;
                case UNSUBSCRIBE:
                    log.info("UNSUBSCRIBE");
                    //	客户端取消订阅
                    //	客户端发送UNSUBSCRIBE报文给服务端，用于取消订阅主题
                    mqttMsgBack.unsubscribeAck(ctx, mqttMessage);
                    break;
                case PINGREQ:
                    log.info("PINGREQ");
                    //	客户端发起心跳
                    mqttMsgBack.pingResp(ctx, mqttMessage);
                    break;
                case DISCONNECT:
                    log.info("DISCONNECT");
                    //	客户端主动断开连接
                    //	DISCONNECT报文是客户端发给服务端的最后一个控制报文， 服务端必须验证所有的保留位都被设置为0
                    /*
                     * TODO  断开连接修改数据库状态 从上下文中移除
                     */
                    log.info("{} client disconnected", ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress());
                    mqttMsgBack.disConnect(ctx, mqttMessage);
                    break;
                // ----------------------服务端作为发送消息端可能会接收的事件----------------------------------------------------------------
                case PUBACK:
                    log.info("PUBACK");
                case PUBREC:
                    log.info("PUBREC");
                    //QoS 2级别,响应一个PUBREL报文消息，PUBACK、PUBREC这俩都是ack消息
                    //PUBACK报文是对QoS 1等级的PUBLISH报文的响应，如果一段时间没有收到客户端ack，服务端会重新发送消息
                    mqttMsgBack.receivePubAck(ctx, mqttMessage);
                    break;
                case PUBCOMP:
                    log.info("PUBCOMP");
                    //收到qos2级别接收端最后一次发送过来的确认消息
                    mqttMsgBack.receivePubcomp(ctx, mqttMessage);
                    break;
                default:
                    log.info("default");
                    break;
            }
        }
    }

    /**
     * 功能描述: 心跳检测
     *
     * @param ctx 这里的作用主要是解决断网，弱网的情况发生
     * @param evt
     * @return void
     * @author zhouwenjie
     * @date 2020/4/3 17:02
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        String socketString = ctx.channel().remoteAddress().toString();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client: " + socketString + " READER_IDLE 读超时");
                delSubCache(ctx);
                ctx.disconnect();
            }
        }
    }


    /**
     * 功能描述:
     *
     * @param ctx
     * @param cause
     * @return void
     * @author 发生异常会触发此函数
     * @date 2020/4/3 16:49
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    private void delSubCache(ChannelHandlerContext ctx){
        String id = ctx.channel().id().toString();
        clientMap.remove(id);
        //删除订阅主题
        Set<String> topicSet = MqttMsgBack.clientTopicMap.get(id);
        if (CollUtil.isNotEmpty(topicSet)) {
            ConcurrentHashMap<String, HashSet<String>> subMap = MqttMsgBack.subMap;
            ConcurrentHashMap<String, MqttQoS> qoSMap = MqttMsgBack.qoSMap;
            for (String topic : topicSet) {
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
            }
        }
        MqttMsgBack.clientTopicMap.remove(id);
    }
}

