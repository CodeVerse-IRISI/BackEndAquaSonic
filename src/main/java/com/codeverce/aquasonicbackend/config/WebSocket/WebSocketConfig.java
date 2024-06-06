package com.codeverce.aquasonicbackend.config.WebSocket;

import com.codeverce.aquasonicbackend.handler.SensorDataWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuration pour WebSocket.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SensorDataWebSocketHandler sensorDataWebSocketHandler;

    @Autowired
    public WebSocketConfig(SensorDataWebSocketHandler sensorDataWebSocketHandler) {
        this.sensorDataWebSocketHandler = sensorDataWebSocketHandler;
    }

    /**
     * Enregistre les gestionnaires WebSocket.
     *
     * @param registry Le registre des gestionnaires WebSocket.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(sensorDataWebSocketHandler, "/ws/sensor-data").setAllowedOrigins("*");
    }
}
