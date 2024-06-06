package com.codeverce.aquasonicbackend.dto;

import lombok.Data;

/**
 * DTO représentant les données d'une appel.
 */
@Data
public class SensorDataDTO {
    private String date;
    private int leak;
    private String time;
    private double[] amplitudes;
}
