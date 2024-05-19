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
    private SensorService sensorService;

    @Autowired
    public KafkaService(SensorDataRepository sensorDataRepository, ObjectMapper objectMapper) {
        this.sensorDataRepository = sensorDataRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sounds", groupId = "serrakhi-group")
    public void consume(String message) {
        try {
            System.out.println("KafkaService.consume: " + message);
            SensorData sensorData = parseSensorData(message);
            processKafkaMessage(sensorData);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    public void processKafkaMessage(SensorData sensorData) {
        saveSensorData(sensorData);
        // Attendre 5 secondes avant de verifier s'il y'a fuite ou non
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sensorService.detectLeakAndUpdateCount(sensorData.getSensor_id());
    }

    private SensorData parseSensorData(String message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(message);
        SensorData sensorData = objectMapper.treeToValue(rootNode, SensorData.class);

        return sensorData;
    }


    private void saveSensorData(SensorData sensorData) {
        sensorDataRepository.save(sensorData);
    }
}
