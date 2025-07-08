package com.mirrormetech.corm.common.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private String brokerUrl;
    private String clientId;
    private String username;
    private String password;
    private int connectionTimeout;
    private int keepAliveInterval;
    private boolean automaticReconnect;
    private boolean cleanSession;
    private String defaultTopic;
}
