package com.mirrormetech.corm.common.server;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
/**
 * Title:Server 这是发送消息的服务端
 * Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * @author rao
 */
public class serverMqtt {

    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://127.0.0.1:1884";
    //定义一个主题
    public static final String TOPIC = "topic";
    //定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientid = "zhujunjian";

    private MqttClient client;
    private MqttTopic topic11;
    private String userName = "admin";  //非必须
    private String passWord = "123456";  //非必须

    private MqttMessage message;

    /**
     * 构造函数
     * @throws MqttException
     */
    public serverMqtt() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    /**
     *  用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(null);
            client.connect(options);

            topic11 = client.getTopic(TOPIC);
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
        System.out.println("message is published completely! "
                + token.isComplete());
    }

    /**
     *  启动入口
     * @param args
     * @throws MqttException
     */
    public static void main(String[] args) throws MqttException {
        serverMqtt server = new serverMqtt();

        server.message = new MqttMessage();
        server.message.setQos(1);  //保证消息能到达一次
        server.message.setRetained(true);
        server.message.setPayload("这是推送消息的内容".getBytes());
        server.publish(server.topic11 , server.message);
        System.out.println(server.message.isRetained() + "------ratained状态");
    }
}

