package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.SensorDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map.Entry;

@Service
public class KafkaService {

    private final SensorDataRepository sensorDataRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaService(SensorDataRepository sensorDataRepository, ObjectMapper objectMapper) {
        this.sensorDataRepository = sensorDataRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sounds", groupId = "serrakhi-group")
    public void consume(String message) {
        try {
            SensorData sensorData = parseSensorData(message);
            processKafkaMessage(sensorData);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    public void processKafkaMessage(SensorData sensorData) {
        saveSensorData(sensorData);
        // I have intention to add some triggers here to be relatable with other functionality
    }

    private SensorData parseSensorData(String message) throws Exception {
        JsonNode rootNode = objectMapper.readTree(message);
        Entry<String, JsonNode> entry = rootNode.fields().next();
        String sensorId = entry.getKey();
        JsonNode sensorDataNode = entry.getValue();
        SensorData sensorData = objectMapper.treeToValue(sensorDataNode, SensorData.class);
        sensorData.setSensor_id(sensorId);
        return sensorData;
    }

    private void saveSensorData(SensorData sensorData) {
        sensorDataRepository.save(sensorData);
    }
}
