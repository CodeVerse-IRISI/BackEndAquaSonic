package com.codeverce.aquasonicbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Modèle représentant la configuration de l'environnement.
 */
@Document(collection = "env_setup")
@Data
public class EnvSetup {
    @Id
    private String id;
    private int depth;
    private int diameter;
    private String material;
    private Boolean branched;
    private Boolean looped;
}
