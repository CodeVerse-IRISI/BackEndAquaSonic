package com.codeverce.aquasonicbackend.service;

import com.codeverce.aquasonicbackend.handler.SensorDataWebSocketHandler;
import com.codeverce.aquasonicbackend.entity.CarteData;
import com.codeverce.aquasonicbackend.entity.SensorData;
import com.codeverce.aquasonicbackend.repository.CarteRepository;
import com.codeverce.aquasonicbackend.repository.SensorDataRepository;
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

/**
 * Service pour gérer les messages Kafka en temp reel et mise a jour les données.
 */
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

    /**
     * Kafka listener pour consommer les messages du topic "sounds".
     *
     * @param message Le message Kafka à consommer.
     */
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

    /**
     * Traite le message Kafka consommé, enregistre et met à jour les rapports du capteur.
     *
     * @param sensorData Les données du capteur à traiter.
     */
    public void processKafkaMessage(SensorData sensorData) {
        saveSensorData(sensorData);
        updateNbOfleak(sensorData);
        updateNbOfRepair(sensorData);
        try {
            broadcastSensorDataToWebSocketClients(sensorData.getSensor_id());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour le nombre de fuites si une fuite est détectée.
     *
     * @param sensorData Les données du capteur.
     */
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

    /**
     * Met à jour le nombre de réparations si une réparation est détectée.
     *
     * @param sensorData Les données du capteur.
     */
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

    /**
     * Analyse le message Kafka entrant en un objet SensorData.
     *
     * @param message Le message Kafka.
     * @return Un objet SensorData.
     * @throws Exception Si une erreur survient lors de l'analyse.
     */
    private SensorData parseSensorData(String message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(message);
        SensorData sensorData = objectMapper.treeToValue(rootNode, SensorData.class);

        return sensorData;
    }

    /**
     * Diffuse les données du capteur à tous les clients WebSocket connectés.
     *
     * @param id L'identifiant du capteur.
     * @throws IOException Si une erreur survient lors de l'envoi du message.
     */
    private void broadcastSensorDataToWebSocketClients(String id) throws IOException {
        Map<String, Double> sensorsGravity = new HashMap<>();
        sensorsGravity.put(id, sensorService.calculateSensorLeakGravity(id));
        String sensorsGravityJson = objectMapper.writeValueAsString(sensorsGravity);
        for (WebSocketSession session : webSocketHandler.getSessions()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(sensorsGravityJson));
            }
        }
    }

    /**
     * Sauvegarde les données du capteur dans la base de données.
     *
     * @param sensorData Les données du capteur à sauvegarder.
     */
    private void saveSensorData(SensorData sensorData) {
        sensorDataRepository.save(sensorData);
    }
}
