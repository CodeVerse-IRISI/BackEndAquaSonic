package com.codeverce.aquasonicbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Capteur {
    @JsonProperty("sensor_id")
    private String sensor_id;

    @JsonProperty("droite_id")
    private String droite_id ;

    @JsonProperty("gauche_id")
    private String gauche_id;

    @JsonProperty("x")
    private double x;

    @JsonProperty("Y")
    private double y;

    // Constructeur
    public Capteur(String sensor_id, double x, double y) {
        this.sensor_id = sensor_id;
        this.x = x;
        this.y = y;
    }

    // Getters et setters pour id
    public String getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(String id) {
        this.sensor_id = sensor_id;
    }

    // Getters et setters pour x
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    // Getters et setters pour y
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
