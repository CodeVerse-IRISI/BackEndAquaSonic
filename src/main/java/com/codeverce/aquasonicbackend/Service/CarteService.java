package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.DTO.Capteur;
import com.codeverce.aquasonicbackend.Model.CarteData;
import com.codeverce.aquasonicbackend.Repository.CarteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarteService {

    private final CarteRepository carteRepository;

    @Autowired
    public CarteService(CarteRepository carteRepository) {
        this.carteRepository = carteRepository;
    }

    @Autowired
    SensorService sensorService;


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
        double leakPercentage = sensorService.calculateRate(sensorId);

        // Déterminer le statut en fonction du pourcentage de fuites
        String status = "non fuite";

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
        resultMap.put("Status", status);

        return resultMap;
    }

}
