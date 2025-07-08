package com.mirrormetech.corm.common.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttClientManager implements DisposableBean {

    private final MqttAsyncClient mqttClient;
    private final MqttProperties properties;

    @Autowired
    public MqttClientManager(MqttProperties properties) throws MqttException {
        this.properties = properties;
        // 使用内存持久化（避免磁盘I/O）
        this.mqttClient = new MqttAsyncClient(
                properties.getBrokerUrl(),
                properties.getClientId(),
                new MemoryPersistence()
        );

        connect();
    }

    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(properties.getUsername());
        options.setPassword(properties.getPassword().toCharArray());
        options.setConnectionTimeout(properties.getConnectionTimeout());
        options.setKeepAliveInterval(properties.getKeepAliveInterval());
        options.setAutomaticReconnect(properties.isAutomaticReconnect());
        options.setCleanSession(properties.isCleanSession());

        // 设置连接监听器
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                System.out.println("MQTT " + (reconnect ? "重新连接" : "初始连接") + "成功: " + serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                System.err.println("MQTT连接丢失: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // 订阅功能留空，此处仅实现发布
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // 消息投递完成回调
            }
        });

        // 异步连接（非阻塞）
        mqttClient.connect(options, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("MQTT连接建立成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.err.println("MQTT连接失败: " + exception.getMessage());
            }
        });
    }

    /**
     * 发送消息到指定主题
     * @param topic 主题名称
     * @param payload 消息内容
     * @param qos 服务质量等级 (0,1,2)
     */
    public void publish(String topic, byte[] payload, int qos) throws MqttException {
        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);
        message.setRetained(false);

        mqttClient.publish(topic, message);
    }

    /**
     * 发送消息到默认主题
     */
    public void publish(byte[] payload) throws MqttException {
        publish(properties.getDefaultTopic(), payload, 0);
    }

    @Override
    public void destroy() throws Exception {
        if (mqttClient != null && mqttClient.isConnected()) {
            mqttClient.disconnect();
            mqttClient.close();
            System.out.println("MQTT客户端已断开连接");
        }
    }
}
