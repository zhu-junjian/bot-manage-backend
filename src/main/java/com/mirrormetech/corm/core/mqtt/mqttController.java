package com.mirrormetech.corm.core.mqtt;

import com.mirrormetech.corm.common.server.MqttBootServer;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author spencer
 * @date 2025/05/19
 */
@Slf4j
@RestController
@RequestMapping("/mqttBackend")
@RequiredArgsConstructor
public class mqttController {

    private final MqttBootServer mqttBootServer;

    @GetMapping("/hello")
    public String hello(@PathParam("name") String name) throws UnknownHostException {
        return "Hello " + name+ " from mqtt server" + InetAddress.getLocalHost().getHostAddress();
    }
}
