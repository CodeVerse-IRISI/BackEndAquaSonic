package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.CarteData;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.CarteRepository;
import com.codeverce.aquasonicbackend.Repository.SensorDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Service
public class KafkaService {

    private final SensorDataRepository sensorDataRepository;
    private final ObjectMapper objectMapper;
    private String date;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private SensorService sensorService;
    @Autowired
    private SensorDataWebSocketHandler webSocketHandler;

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
        System.out.printf("sensordata yyyy :"+sensorData);
        saveSensorData(sensorData);
        updateNbOfleak(sensorData);
        updateNbOfRepair(sensorData);
        try {
            broadcastSensorDataToWebSocketClients(sensorData.getSensor_id());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateNbOfleak(SensorData sensorData){
        CarteData sensor = carteRepository.findBySensorId(sensorData.getSensor_id());
        System.out.println("sensor:" + sensor);
        if (sensorData.getLeak() == 1) {
            date = sensor.getDateLastFuite();
            System.out.println("date:" + date);
            if (date != null && !date.equals(sensorData.getDate())) {
                sensorService.detectLeakAndUpdateCount(sensorData.getSensor_id(), sensorData.getDate());
                }
            }
        }
    public void updateNbOfRepair(SensorData sensorData){
        CarteData sensor = carteRepository.findBySensorId(sensorData.getSensor_id());
        LocalDate dateLastLeak = LocalDate.parse(sensor.getDateLastFuite(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate currentDate = LocalDate.parse(sensorData.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Period period = Period.between(dateLastLeak, currentDate);
        if (sensorData.getLeak() == 0 && period.getDays() == 1) {
            sensor.setNb_reparation(sensor.getNb_reparation() + 1);
            carteRepository.save(sensor);
        }
    }

        private SensorData parseSensorData (String message) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(message);
            SensorData sensorData = objectMapper.treeToValue(rootNode, SensorData.class);

            return sensorData;
        }

    private void broadcastSensorDataToWebSocketClients(String id) throws IOException {
        //return a json with All Sensor Degree Gravity
        Map<String, Double> sensorsGravity = new HashMap<>();
        sensorsGravity.put(id, sensorService.calculateSensorLeakGravity(id));
        String sensorsGravityJson = objectMapper.writeValueAsString(sensorsGravity);
        for (WebSocketSession session : webSocketHandler.getSessions()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(sensorsGravityJson));
            }
        }
    }


    private void saveSensorData(SensorData sensorData) {
        sensorDataRepository.save(sensorData);
    }
}
