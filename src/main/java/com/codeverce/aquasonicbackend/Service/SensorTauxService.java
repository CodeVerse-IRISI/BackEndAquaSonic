package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.SensorTauxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;


import org.springframework.data.domain.PageRequest;

@Service
public class SensorTauxService {

    @Autowired
    private SensorTauxRepository sensorTauxRepository;

    public ResponseEntity<String> getLeakStatusForSensor(String sensor_id) {
        // Définir un objet Pageable pour récupérer les premiers trois résultats
        Pageable pageable = PageRequest.of(0, 3);

        // Récupérer tous les enregistrements triés par date pour le capteur spécifié
        List<SensorData> allData = sensorTauxRepository.findAllBySensorIdOrderByDateAsc(sensor_id, pageable);

        if (allData.isEmpty()) {
            System.out.println("Aucune donnée n'a été récupérée pour le capteur avec l'ID : " + sensor_id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Limiter les résultats aux trois premiers enregistrements
        List<SensorData> firstThreeData = allData.subList(0, Math.min(allData.size(), 3));

        // Compter le nombre de valeurs true parmi les trois premières
        long trueCount = firstThreeData.stream().filter(data -> data.getLeak() == 1).count();

        // Si au moins deux des trois premières valeurs sont true, récupérer la quatrième valeur
        int leakStatus = 90; // Par défaut, supposons qu'il n'y a pas de fuite
        if (trueCount >= 2 && allData.size() >= 4) {
            SensorData fourthData = allData.get(3); // Récupérer la quatrième valeur
            if (fourthData.getLeak() == 1) {
                leakStatus = 70; // Si la quatrième valeur est true, la fuite est détectée
            }
        }

        return ResponseEntity.ok(leakStatus+" % une fuite");
    }
}



