package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.DTO.Capteur;
import com.codeverce.aquasonicbackend.Model.CarteData;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.CarteRepository;
import com.codeverce.aquasonicbackend.Repository.SensorTauxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarteService {

    private final CarteRepository carteRepository;
    private final SensorTauxRepository sensorTauxRepository;

    @Autowired
    public CarteService(CarteRepository carteRepository, SensorTauxRepository sensorTauxRepository) {
        this.carteRepository = carteRepository;
        this.sensorTauxRepository = sensorTauxRepository;
    }



    public List<Capteur> getAllCapteurs() {
        List<CarteData> carteDataList = carteRepository.getAllCapteurs();
        return carteDataList.stream()
                .map(carteData -> new Capteur(carteData.getSensor_id(), carteData.getX(), carteData.getY()))
                .collect(Collectors.toList());
    }


    public Map<String, Object> findInformationSensorIdWithAdditionalField(String sensorId) {
        CarteData carteData = carteRepository.findBySensorId(sensorId);
        if (carteData == null) {
            return null;
        }

        // Obtenir le pourcentage de fuites pour le capteur spécifié
        double leakPercentage = getLeakPercentageForSensor(sensorId);

        // Déterminer le statut en fonction du pourcentage de fuites
        String status = "non fuite"; // par défaut

        if (leakPercentage > 50) {
            status = "fuite";
        }

        // Créer un objet Map pour stocker les champs de CarteData avec le champ supplémentaire
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("sensor_id", carteData.getSensor_id());

        resultMap.put("droite_id", carteData.getDroite_id());
        resultMap.put("gauche_id", carteData.getGauche_id());
        resultMap.put("nb_fuite", carteData.getNb_fuite());
        resultMap.put("nb_reparation", carteData.getNb_reparation());

        // Ajouter le pourcentage de fuites comme champ supplémentaire
        resultMap.put("Status", status);

        return resultMap;
    }
    private double getLeakPercentageForSensor(String sensorId) {
        // Définir un objet Pageable pour récupérer les premiers quatre résultats
        Pageable pageable = PageRequest.of(0, 4);

        // Récupérer tous les enregistrements triés par date pour le capteur spécifié
        List<SensorData> allData = sensorTauxRepository.findAllBySensorIdOrderByDateAsc(sensorId, pageable);

        if (allData.isEmpty()) {
            System.out.println("Aucune donnée n'a été récupérée pour le capteur avec l'ID : " + sensorId);
            return 0.0; // Retourner 0 si aucune donnée n'est trouvée
        }

        // Limiter les résultats aux quatre premiers enregistrements
        List<SensorData> firstFourData = allData.subList(0, Math.min(allData.size(), 4));

        // Compter le nombre total de fuites parmi les quatre premiers enregistrements
        long totalLeaks = firstFourData.stream().filter(data -> data.getLeak() == 1).count();

        // Calculer le pourcentage de fuites
        double leakPercentage = (double) (totalLeaks * 100) / 4;

        return leakPercentage;
    }



}
