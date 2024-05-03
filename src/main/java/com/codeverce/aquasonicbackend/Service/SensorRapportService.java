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


        // Récupérer le premier enregistrement trié par date pour le capteur spécifié
        public ResponseEntity<Map<String, Map<String, List<Boolean>>>> getSensorRapportForDate(String sensor_id) {
            // Récupérer tous les enregistrements triés par date pour le capteur spécifié
            List<SensorData> sortedDataList = sensorRapportRepository.findFirstBySensorIdOrderByDate(sensor_id);

            if (sortedDataList.isEmpty()) {
                System.out.println("Aucune donnée n'a été récupérée pour le capteur avec l'ID : " + sensor_id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Récupérer le premier enregistrement de la liste triée
            SensorData firstData = sortedDataList.get(0);

            // Convertir la date de l'enregistrement en LocalDate
            LocalDate startDate = LocalDate.parse(firstData.getDate(), DateTimeFormatter.ISO_DATE);
            // Calculer la date de fin en ajoutant 7 jours à la date de début
            LocalDate endDate = startDate.plusDays(7);

        // Récupérer les données pour le capteur spécifié et dans la plage de dates calculée
        List<SensorData> sensorDataList = sensorRapportRepository.findBySensorIdAndDateBetween(sensor_id, startDate.toString(), endDate.toString());

        // Traitement des données
        Map<String, Map<String, List<Boolean>>> sensorRapportMap = new HashMap<>();
        Map<String, List<Boolean>> sensorDataMap = new HashMap<>();

        for (SensorData sensorData : sensorDataList) {
            String date = sensorData.getDate();
            boolean leak = sensorData.getLeak() == 1;
            List<Boolean> leaks = sensorDataMap.getOrDefault(date, new ArrayList<>());
            leaks.add(leak);
            sensorDataMap.put(date, leaks);
        }

        sensorRapportMap.put(sensor_id, sensorDataMap);

        // Affichage des données (optionnel)
        for (Map.Entry<String, Map<String, List<Boolean>>> entry : sensorRapportMap.entrySet()) {
            String sensorId = entry.getKey();
            Map<String, List<Boolean>> dataMap = entry.getValue();
            for (Map.Entry<String, List<Boolean>> dataEntry : dataMap.entrySet()) {
                String date = dataEntry.getKey();
                List<Boolean> leaks = dataEntry.getValue();
                System.out.println("Sensor ID: " + sensor_id + ", Date: " + date + ", Leaks: " + leaks);
            }
        }

        return ResponseEntity.ok(sensorRapportMap);
    }
}

