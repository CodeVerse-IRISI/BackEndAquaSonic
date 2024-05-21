package com.codeverce.aquasonicbackend.Config.WebSocket;

import com.codeverce.aquasonicbackend.Service.SensorDataWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SensorDataWebSocketHandler sensorDataWebSocketHandler;

    @Autowired
    public WebSocketConfig(SensorDataWebSocketHandler sensorDataWebSocketHandler) {
        this.sensorDataWebSocketHandler = sensorDataWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(sensorDataWebSocketHandler, "/ws/sensor-data").setAllowedOrigins("*");
    }
}