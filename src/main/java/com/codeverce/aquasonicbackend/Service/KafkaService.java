package com.codeverce.aquasonicbackend.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sounds", groupId = "serrakhi-group")
    public void consume(String message) {
        try {
            // Parse the JSON message
            JsonNode jsonNode = objectMapper.readTree(message);
            //do what we want with the message now (jsonNode)
            System.out.println("Recieved Message:"+ jsonNode);
            // Fetch the sensor ID
            String sensorId = jsonNode.fieldNames().next();

            // Fetch the "leak" value
            int leakValue = jsonNode.path(sensorId).get("leak").asInt();

            // Print message depending on the "leak" value
            if (leakValue == 1) {
                System.out.println("Leak detected in sensor: " + sensorId);
            } else {
                System.out.println("No leak detected.");
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
