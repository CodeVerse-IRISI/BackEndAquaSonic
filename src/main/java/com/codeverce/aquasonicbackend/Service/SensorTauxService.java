package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.SensorTauxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class SensorTauxService {

    @Autowired
    private SensorTauxRepository sensorTauxRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponseEntity<Map<String, Double>> getAllLeakStatus() {

        List<SensorTauxRepository.SensorIdProjection> allSensorIds = sensorTauxRepository.findAllSensorIds();

        if (allSensorIds.isEmpty()) {
            System.out.println("Aucune donnée n'a été récupérée pour les capteurs.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Créer une carte pour stocker les pourcentages de fuite correspondants à chaque ID de capteur
        Map<String, Double> sensorLeakStatusMap = new HashMap<>();

        // Parcourir chaque ID de capteur et calculer le pourcentage de fuite correspondant
        for (SensorTauxRepository.SensorIdProjection sensorIdProjection : allSensorIds) {
            String sensorId = sensorIdProjection.getSensor_id();
            double leakPercentage = getLeakPercentageForSensor(sensorId);
            sensorLeakStatusMap.put(sensorId, leakPercentage);
        }

        return ResponseEntity.ok(sensorLeakStatusMap);
    }

    private double getLeakPercentageForSensor(String sensor_id) {
        // Définir un objet Pageable pour récupérer les premiers quatre résultats
        Pageable pageable = PageRequest.of(0, 4);

        // Récupérer tous les enregistrements triés par date pour le capteur spécifié
        List<SensorData> allData = sensorTauxRepository.findAllBySensorIdOrderByDateAsc(sensor_id, pageable);

        if (allData.isEmpty()) {
            System.out.println("Aucune donnée n'a été récupérée pour le capteur avec l'ID : " + sensor_id);
            return 0.0; // Retourner 0 si aucune donnée n'est trouvée
        }

        // Limiter les résultats aux quatre premiers enregistrements
        List<SensorData> firstFourData = allData.subList(0, Math.min(allData.size(), 4));

        // Compter le nombre total de fuites parmi les quatre premiers enregistrements
        long totalLeaks = firstFourData.stream().filter(data -> data.getLeak() == 1).count();

        // Calculer le pourcentage de fuites
        return (double) (totalLeaks * 100) / 4;
    }

    public double getLeaknb_sensourForSensor(String sensor_id) {
        // Définir un objet Pageable pour récupérer les premiers quatre résultats
        Pageable pageable = PageRequest.of(0, 4);

        // Récupérer tous les enregistrements triés par date pour le capteur spécifié
        List<SensorData> allData = sensorTauxRepository.findAllBySensorIdOrderByDateAsc(sensor_id, pageable);

        if (allData.isEmpty()) {
            System.out.println("Aucune donnée n'a été récupérée pour le capteur avec l'ID : " + sensor_id);
            return 0.0;
        }

        // Limiter les résultats aux quatre premiers enregistrements
        List<SensorData> firstFourData = allData.subList(0, Math.min(allData.size(), 4));

        // Compter le nombre total de fuites parmi les quatre premiers enregistrements
        long totalLeaks = firstFourData.stream().filter(data -> data.getLeak() == 1).count();

        // Calculer le pourcentage de fuites
        double leakPercentage = (double) (totalLeaks * 100) / 4;

        if (leakPercentage > 50) {
            try {
                Query query = new Query(Criteria.where("sensor_id").is(sensor_id));
                Update update = new Update().inc("nb_fuite", 1);


                mongoTemplate.updateFirst(query, update, "rapport");
                System.out.println("Nombre de fuite incrémenté avec succès pour le capteur " + sensor_id);

                // Ajoutez le message indiquant que des fuites réparées ont été trouvées

            } catch (Exception e) {
                e.printStackTrace();
                // Gérer les erreurs MongoDB
            }
        }
        return leakPercentage; // ou toute autre valeur que vous souhaitez renvoyer
    }

}
