package com.codeverce.aquasonicbackend.service;

import com.codeverce.aquasonicbackend.dto.Capteur;
import com.codeverce.aquasonicbackend.entity.CarteData;
import com.codeverce.aquasonicbackend.repository.CarteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les données des cartes et des capteurs.
 */
@Service
public class CarteService {

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    public CarteService(CarteRepository carteRepository) {
        this.carteRepository = carteRepository;
    }

    @Autowired
    SensorService sensorService;

    /**
     * Récupère les coordonnées de tous les capteurs.
     *
     * @return Liste des capteurs.
     */
    public List<Capteur> getAllCapteurs() {
        List<CarteData> carteDataList = carteRepository.getAllCapteurs();
        return carteDataList.stream()
                .map(carteData -> new Capteur(carteData.getSensor_id(), carteData.getX(), carteData.getY()))
                .collect(Collectors.toList());
    }

    /**
     * Trouve les informations d'un capteur spécifique par son ID.
     *
     * @param sensorId L'identifiant du capteur.
     * @return Les informations du capteur.
     */
    public CarteData findInformationSensorId(String sensorId) {
        return carteRepository.findBySensorId(sensorId);
    }

    /**
     * Sauvegarde les données d'un capteur.
     *
     * @param carteData Les données de la carte à sauvegarder.
     * @return Les données de la carte sauvegardées.
     */
    public CarteData saveCarteData(CarteData carteData) {
        return carteRepository.save(carteData);
    }
}
