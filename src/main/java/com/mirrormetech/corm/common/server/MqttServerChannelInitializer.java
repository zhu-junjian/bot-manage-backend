package com.mirrormetech.corm.common.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MqttServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ServerMqttHandler serverMqttHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new MqttDecoder(1024 * 8));
        pipeline.addLast("encoder", MqttEncoder.INSTANCE);
        /*
         * TODO 超时时间待测试
         */
        //pipeline.addLast("", new IdleStateHandler(60, 0, 0));
        pipeline.addLast(serverMqttHandler);
    }
}
