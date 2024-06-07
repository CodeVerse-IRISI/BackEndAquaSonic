package com.codeverce.aquasonicbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Modèle représentant les informations pour afficher un capteur dans une carte.
 */
@Document(collection = "Informations")
@Data
public class  CarteData {
    @Id
    private String id;
    private String sensor_id;
    private double X;
    private double Y;
    private String droite_id;
    private String gauche_id;
    private String DateLastFuite;
    private int nb_fuite;
    private int nb_reparation;
}
