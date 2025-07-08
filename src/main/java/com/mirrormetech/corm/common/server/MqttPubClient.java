package com.mirrormetech.corm.common.server;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

/**
 * MQTT发布客户端
 * 全局单例
 * corm内部处理mqtt协议的发送客户端
 */
@Component
public class MqttPubClient {

    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://47.110.242.137:1883";
    //定义一个主题
    public static final String TOPIC = "topic";
    //定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientId = "corm-backend-client";

    @Getter
    private MqttClient client;
    private MqttTopic topic11;
    private String userName = "admin";  //非必须
    private String passWord = "123456";  //非必须

    private MqttMessage message;


    /**
     * 构造函数
     * @throws MqttException
     */
    public MqttPubClient() throws MqttException {
        // MemoryPersistence设置clientId的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientId, new MemoryPersistence());
        connect();
    }

    /**
     *  用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(60);
        try {
            client.setCallback(null);
            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
    }

}
