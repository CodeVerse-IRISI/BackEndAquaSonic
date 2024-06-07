package com.codeverce.aquasonicbackend.controller;

import com.codeverce.aquasonicbackend.dto.SensorDataDTO;
import com.codeverce.aquasonicbackend.entity.SensorData;
import com.codeverce.aquasonicbackend.service.KafkaService;
import com.codeverce.aquasonicbackend.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour gérer les opérations liées aux données des capteurs.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/AquaSonic")
public class SensorController {

    private final SensorService sensorService;
    private final KafkaService kafkaService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SensorController(SensorService sensorService, KafkaService kafkaService) {
        this.sensorService = sensorService;
        this.kafkaService = kafkaService;
    }

    /**
     * Récupère toutes les données d'un capteur spécifique.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return La liste des données du capteur.
     */
    @GetMapping("/SensorData/{sensor_id}")
    public List<SensorData> getListDataBySensorId(@PathVariable String sensor_id) {
        return sensorService.getAllSensorData(sensor_id);
    }

    /**
     * Récupère les données d'un capteur spécifique à une date donnée.
     *
     * @param sensor_id L'identifiant du capteur.
     * @param dateStr   La date au format "yyyy-MM-dd".
     * @return Les données du capteur pour la date spécifiée.
     */
    @GetMapping("/SensorDataByDate/{sensor_id}/{dateStr}")
    public List<SensorDataDTO> getListData(@PathVariable String sensor_id, @PathVariable String dateStr) {
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sensorService.getSensorData(sensor_id, date);
    }

    /**
     * Récupère les données du capteur pour aujourd'hui.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return Les données du capteur pour aujourd'hui.
     */
    @GetMapping("/SensorDataForToday/{sensor_id}")
    public List<SensorDataDTO> GetSensorDataForToday(@PathVariable String sensor_id) {
        return sensorService.getSensorData(sensor_id);
    }

    /**
     * Récupère le taux de réalité des fuites détectées par un capteur aujourd'hui.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return Le taux de réalité des fuites détectées par le capteur aujourd'hui.
     */
    @GetMapping("/rateLeak/{sensor_id}")
    public double getSensorRateLeak(@PathVariable String sensor_id){
        return sensorService.calculateRate(sensor_id);
    }

    /**
     * Récupère le taux de gravité des fuites qui dépassent un seuil pour un capteur aujourd'hui.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return Le taux de gravité des fuites qui dépassent un seuil pour le capteur aujourd'hui.
     */
    @GetMapping("/SeriousDegreeLeak/{sensor_id}")
    public double GetSensorGravityRate(@PathVariable String sensor_id){
        return sensorService.calculateSensorLeakGravity(sensor_id);
    }

    /**
     * Récupère la gravité de toutes les fuites détectées par les capteurs surveillés.
     *
     * @return La gravité de toutes les fuites détectées par les capteurs surveillés.
     */
    @GetMapping("/AllSensorsDegreeLeak")
    public ResponseEntity<Map<String, Double>> GetAllSensorsGravityLeak(){
        Map<String, Double> sensorsGravity = sensorService.AllSensorDegreeGravity();
        return ResponseEntity.ok(sensorsGravity);
    }

    /**
     * Récupère le taux de réalité de toutes les fuites détectées par les capteurs surveillés.
     *
     * @return Le taux de réalité de toutes les fuites détectées par les capteurs surveillés.
     */
    @GetMapping("/Couleur/leakStatus")
    public ResponseEntity<Map<String, Double>> GetAllSensorsRateLeak(){
        Map<String, Double> sensorsRateLeak = sensorService.AllSensorsRateLeak();
        return ResponseEntity.ok(sensorsRateLeak);
    }

    /**
     * Récupère les données d'un capteur pour un nombre donné de jours.
     *
     * @param sensorId     L'identifiant du capteur.
     * @param numberOfDays Le nombre de jours.
     * @return Les données du capteur pour le nombre de jours spécifié.
     */
    @GetMapping("/GetSensorDataForNDays/{sensor_id}/{numberOfDays}")
    public List<SensorData> GetSensorDataForNDays(@PathVariable("sensor_id") String sensorId, @PathVariable("numberOfDays") int numberOfDays) {
        return sensorService.getSensorData(sensorId, numberOfDays);
    }

    /**
     * Incrémente le nombre de fuites détectées par un capteur.
     *
     * @param sensorData Les données du capteur.
     */
    @PostMapping("/incrementNbLeak")
    public void incrementNbLeak(@RequestBody SensorData sensorData){
        kafkaService.updateNbOfleak(sensorData);
    }

    /**
     * Incrémente le nombre de réparations effectuées par un capteur.
     *
     * @param sensorData Les données du capteur.
     */
    @PostMapping("/incrementNbRepair")
    public void incrementNbRepair(@RequestBody SensorData sensorData){
        kafkaService.updateNbOfRepair(sensorData);
    }

}