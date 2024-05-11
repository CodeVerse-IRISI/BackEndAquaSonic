package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.SensorRapportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SensorRapportService {

    @Autowired
    private SensorRapportRepository sensorRapportRepository;

    private final SensorTauxService sensorTauxService;

    public SensorRapportService(SensorRapportRepository sensorRapportRepository, SensorTauxService sensorTauxService) {
        this.sensorRapportRepository = sensorRapportRepository;
        this.sensorTauxService = sensorTauxService;
    }

    public ResponseEntity<Map<String, Map<String, List<String>>>> getSensorRapportForDate(String sensor_id) {
        // Récupérer le dernier enregistrement pour le capteur spécifié
        List<SensorData> sortedDataList = sensorRapportRepository.findFirstBySensorIdOrderByDate(sensor_id);

        if (sortedDataList.isEmpty()) {
            System.out.println("Aucune donnée n'a été récupérée pour le capteur avec l'ID : " + sensor_id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Récupérer le dernier enregistrement de la liste triée
        SensorData lastData = sortedDataList.get(sortedDataList.size() - 1);

        // Convertir la date de l'enregistrement en LocalDate
        LocalDate endDate = LocalDate.parse(lastData.getDate(), DateTimeFormatter.ISO_DATE);

        // Calculer la date de début en soustrayant 7 jours à la date de fin
        LocalDate startDate = endDate.minusDays(7);

        // Récupérer les données pour le capteur spécifié et dans la plage de dates calculée
        List<SensorData> sensorDataList = sensorRapportRepository.findBySensorIdAndDateBetween(sensor_id, startDate.toString(), endDate.toString());

        // Traitement des données
        Map<String, Map<String, List<String>>> sensorRapportMap = new HashMap<>();
        Map<String, List<String>> sensorDataMap = new HashMap<>();

        for (SensorData sensorData : sensorDataList) {
            String date = sensorData.getDate();
            boolean leak = sensorData.getLeak() == 1;
            List<String> leaks = sensorDataMap.getOrDefault(date, new ArrayList<>());
            leaks.add(String.valueOf(leak));
            sensorDataMap.put(date, leaks);
        }

        // Appel de getLeakStatusForSensor pour chaque date
        for (Map.Entry<String, List<String>> entry : sensorDataMap.entrySet()) {
            String date = entry.getKey();
            List<String> leaks = entry.getValue();
            //ResponseEntity<String> leakStatusResponse = sensorTauxService.getLeakStatusForSensor(sensor_id);
            //String leakStatus = leakStatusResponse.getBody();

            // Affichage du statut des fuites
           // System.out.println("Sensor ID: " + sensor_id + ", Date: " + date + ", Leaks: " + leaks + ", Leak Status: " + leakStatus);

            // Ajout du statut des fuites à sensorRapportMap
            sensorRapportMap.computeIfAbsent(sensor_id, k -> new HashMap<>()).put(date, new ArrayList<>());
           // sensorRapportMap.get(sensor_id).get(date).add(leakStatus);
        }

        return ResponseEntity.ok(sensorRapportMap);
    }
}
