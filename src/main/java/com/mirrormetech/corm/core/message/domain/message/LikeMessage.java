package com.mirrormetech.corm.core.message.domain.message;

import com.alibaba.fastjson.JSON;
import com.mirrormetech.corm.core.message.domain.Message;
import com.mirrormetech.corm.core.message.domain.content.LikeContent;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttException;

@Data
public class LikeMessage extends Message {

    private LikeContent content;

    public String topic(){
        return  "/corm/user/"+getReceiverId()+"/like";
    }

    @Override
    public String getContent() {
        return JSON.toJSONString(content);
    }

    /**
     * TODO 此部分只执行和消息域有关的逻辑  其余的逻辑应该通过领域服务跨域编排领域能力
     */
    @Override
    public void send() {
        /*
         * 1.先更新数据库  数据库事务提交后再执行后续操作
         * 2.更新缓存数据
         * 3.发送mqtt
         * 4.发送友盟
         */

    }

    /**
     * 将消息推送至第三方平台 友盟 向接收方发送
     * @param message 消息实体
     */
    public void publish(Message message){

    }

    /**
     * 持久化message数据
     * @param message 消息实体 包含了基本参数 例如sessionId
     */
    public void persistence(Message message){
        //TODO 保存至数据库
    }

    /**
     * 更新缓存中的未读数
     * 调用底层防腐层的接口 于具体实现解耦
     */
    public void updateCacheCount(){
        //TODO
    }

    /**
     * 将消息发送至topic
     * @param message 消息实体
     * @param topic mqtt 主题
     */
    public void sendMqtt(Message message, String topic) throws MqttException {
        //TODO 逻辑完善
        /*MqttMessage mqttMessage = new MqttMessage();
        //保证消息能到达一次
        mqttMessage.setQos(2);
        mqttMessage.setRetained(false);

        mqttMessage.setPayload(toJson(message).getBytes());
        MqttClient client = mqttPubClient.getClient();
        MqttTopic mqttTopic = client.getTopic(topic);
        mqttPubClient.publish(mqttTopic, mqttMessage);*/
    }

    /**
     * 将消息实体转换为json
     * transform message to json
     * @param message 消息实体
     * @return json type
     */
    public String toJson(Message message) {
        //TODO 将消息实体转换为json
        return null;
    }
}
