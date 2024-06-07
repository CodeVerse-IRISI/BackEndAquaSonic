package com.codeverce.aquasonicbackend.controller;

import com.codeverce.aquasonicbackend.entity.EnvSetup;
import com.codeverce.aquasonicbackend.service.EnvSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer les opérations liées à la configuration de l'environnement.
 */
@RestController
@RequestMapping("/api/env")
@CrossOrigin
public class EnvSetupController {

    private final EnvSetupService envSetupService;

    @Autowired
    public EnvSetupController(EnvSetupService envSetupService) {
        this.envSetupService = envSetupService;
    }

    /**
     * Enregistre la configuration de l'environnement.
     *
     * @param envSetup La configuration de l'environnement à enregistrer.
     * @return La configuration de l'environnement enregistrée.
     */
    @PostMapping("/SaveEnvSetup")
    public ResponseEntity<EnvSetup> saveEnvSetup(@RequestBody EnvSetup envSetup) {
        EnvSetup savedEnvSetup = envSetupService.saveEnvSetup(envSetup);
        return new ResponseEntity<>(savedEnvSetup, HttpStatus.CREATED);
    }
}
