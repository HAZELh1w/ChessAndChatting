package com.example.cc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author hazel
 * @description 功能描述
 * @create 2022/11/18 8:30
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter severEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
