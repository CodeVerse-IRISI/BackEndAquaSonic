package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.SensorDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private SensorDataRepository sensorDataRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sounds", groupId = "serrakhi-group")
    public void consume(String message) {
        try {
            // Parse the JSON message
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(message);

            // Iterate over each entry in the JSON message
            rootNode.fields().forEachRemaining(entry -> {
                String sensorId = entry.getKey();
                JsonNode sensorDataNode = entry.getValue();

                try {
                    // Map the sensorDataNode to a SensorData object
                    SensorData sensorData = objectMapper.treeToValue(sensorDataNode, SensorData.class);

                    // Set the sensor_id from the key
                    sensorData.setSensor_id(sensorId);

                    // Save the SensorData object to MongoDB
                    sensorDataRepository.save(sensorData);
                } catch (Exception e) {
                    System.err.println("Error processing message for sensor ID " + sensorId + ": " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    public void processKafkaMessage(SensorData sensorData) {
        sensorDataRepository.save(sensorData);
    }

}
