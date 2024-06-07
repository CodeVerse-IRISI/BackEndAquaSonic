package com.codeverce.aquasonicbackend.service;

import com.codeverce.aquasonicbackend.entity.EnvSetup;
import com.codeverce.aquasonicbackend.repository.EnvSetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer la configuration de l'environnement.
 */
@Service
public class EnvSetupService {
    private final EnvSetupRepository envSetupRepository;

    @Autowired
    public EnvSetupService(EnvSetupRepository envSetupRepository) {
        this.envSetupRepository = envSetupRepository;
    }

    /**
     * Sauvegarde les données de configuration de l'environnement.
     *
     * @param envSetup Les données de configuration à sauvegarder.
     * @return Les données de configuration sauvegardées.
     */
    public EnvSetup saveEnvSetup(EnvSetup envSetup) {
        return envSetupRepository.save(envSetup);
    }
}
