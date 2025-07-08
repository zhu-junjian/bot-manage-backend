package com.mirrormetech.corm.common.mqtt;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Bean
    public MqttClientManager mqttClientManager(MqttProperties properties) throws MqttException {
        return new MqttClientManager(properties);
    }
}