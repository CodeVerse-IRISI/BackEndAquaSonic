package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.ReparationDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReparationDataService {
    @Autowired
    private ReparationDataRepository reparationDataRepository;

    public String checkReparation(String sensorId) {
        // Récupérer toutes les données pour le capteur spécifié
        List<SensorData> sensorDataList = reparationDataRepository.findBySensorIdOrderByDate(sensorId);

        // Parcourir les données pour chaque date
        for (SensorData data : sensorDataList) {
            // Récupérer toutes les données pour la même date et le même capteur
            List<SensorData> dataListForDate = reparationDataRepository.findBySensorIdAndDateOrderByDate(sensorId, data.getDate());
            // Compter le nombre de fuites non réparées pour cette date
            int unrepairedLeakCount = 0;
            for (SensorData d : dataListForDate) {
                if (d.getLeak() == 1) { // Vérifier si ce n'est pas une fuite réparée
                    unrepairedLeakCount++;
                }
            }
            // Vérifier si au moins trois fuites non réparées sont présentes pour cette date
            if (unrepairedLeakCount >= 3) {
                return " la  fuites non réparées ont été trouvées pour la date " + data.getDate() + " du capteur " + sensorId + ".";
            }
            else{
                return " la  fuites  réparées  pour la date " + data.getDate() + " du capteur " + sensorId + ".";
            }
        }

        // Si aucune date n'a au moins trois fuites non réparées, afficher le message correspondant
        return " On a besoin de plus  de detail pour avoir une decision .";
    }
}
