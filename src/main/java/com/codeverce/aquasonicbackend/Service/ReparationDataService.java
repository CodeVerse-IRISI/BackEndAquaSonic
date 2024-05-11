package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.ReparationDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReparationDataService {
    @Autowired
    private ReparationDataRepository reparationDataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<String> checkReparation(String sensorId) {
        // Récupérer toutes les données pour le capteur spécifié
        List<SensorData> sensorDataList = reparationDataRepository.findBySensorIdOrderByDate(sensorId);

        // Map pour stocker les dates et le nombre de fuites non réparées pour chaque date
        Map<String, Integer> unrepairedLeaksMap = new HashMap<>();

        // Parcourir les données pour chaque date
        for (SensorData data : sensorDataList) {
            String currentDate = data.getDate();
            // Vérifier si la date est déjà présente dans la map
            if (unrepairedLeaksMap.containsKey(currentDate)) {
                // Si oui, incrémenter le nombre de fuites non réparées
                unrepairedLeaksMap.put(currentDate, unrepairedLeaksMap.get(currentDate) + data.getLeak());
            } else {
                // Sinon, ajouter la date avec le nombre de fuites non réparées actuel
                unrepairedLeaksMap.put(currentDate, data.getLeak());
            }
        }

        List<String> messages = new ArrayList<>();

        // Parcourir la map pour ajouter les messages à la liste
        for (Map.Entry<String, Integer> entry : unrepairedLeaksMap.entrySet()) {
            String currentDate = entry.getKey();
            int unrepairedLeakCount = entry.getValue();
            // Vérifier si au moins deux fuites non réparées sont présentes pour cette date
            if (unrepairedLeakCount > 1) {
                messages.add("Des fuites non réparées ont été trouvées pour la date " + currentDate + " du capteur " + sensorId + ".");
            } else {
                // Ajoutez la logique pour mettre à jour le nombre de réparations dans la collection "rapport" de MongoDB
                try {
                    Query query = new Query(Criteria.where("sensor_id").is(sensorId));
                    Update update = new Update().inc("nb_reparation", 1);
                    mongoTemplate.updateFirst(query, update, "rapport");
                    System.out.println("Nombre de réparations incrémenté avec succès pour le capteur " + sensorId);

                    // Ajoutez le message indiquant que des fuites réparées ont été trouvées
                    messages.add("Des fuites réparées ont été trouvées pour la date " + currentDate + " du capteur " + sensorId + ".");

                } catch (Exception e) {
                    e.printStackTrace();
                    // Gérer les erreurs MongoDB
                }
            }
        }

        // Retourner la liste de messages
        return messages;
    }
}
