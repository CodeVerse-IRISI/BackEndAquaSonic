package com.codeverce.aquasonicbackend.controller;

import com.codeverce.aquasonicbackend.dto.Capteur;
import com.codeverce.aquasonicbackend.entity.CarteData;
import com.codeverce.aquasonicbackend.service.CarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux capteurs.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class CarteController {

    private final CarteService carteService;

    @Autowired
    public CarteController(CarteService carteService){
        this.carteService= carteService;
    }

    /**
     * Enregistre les informations d'un capteur.
     *
     * @param carteData Les informations du capteur à enregistrer.
     * @return Les informations du capteur enregistrées.
     */
    @PostMapping("/SaveInfoSensor")
    public ResponseEntity<CarteData> saveCarteData(@RequestBody CarteData carteData) {
        CarteData savedCarteData = carteService.saveCarteData(carteData);
        return new ResponseEntity<>(savedCarteData, HttpStatus.CREATED);
    }

    /**
     * Récupère tous les capteurs installés avec leurs positions.
     *
     * @return La liste des capteurs avec leurs positions.
     */
    @GetMapping("/capteurs")
    public List<Capteur> getAllCapteurs() {
        return carteService.getAllCapteurs();
    }

    /**
     * Récupère les détails d'un capteur spécifique.
     *
     * @param sensorId L'identifiant du capteur.
     * @return Les détails du capteur spécifié.
     */
    @GetMapping("/information/{sensor_id}")
    public CarteData getSensorInformation(@PathVariable("sensor_id") String sensorId) {
        return carteService.findInformationSensorId(sensorId);
    }
}
