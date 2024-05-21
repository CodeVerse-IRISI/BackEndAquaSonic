package com.codeverce.aquasonicbackend.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "rapport")
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