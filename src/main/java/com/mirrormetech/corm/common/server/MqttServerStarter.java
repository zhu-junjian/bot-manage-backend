package com.mirrormetech.corm.common.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttServerStarter implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private MqttBootServer mqttBootServer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Application started,commencing mqtt server...");
        //mqttBootServer.start();
    }

}
