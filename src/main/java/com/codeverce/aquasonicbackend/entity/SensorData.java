package com.codeverce.aquasonicbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

/**
 * Modèle représentant les données d'un capteur.
 */
@Document(collection = "messages")
@Data
public class SensorData {
    @Id
    private String id;
    private String sensor_id;
    private String date;
    private int leak;
    private String time;
    private double[] amplitudes;

    @Override
    public String toString() {
        return "SensorData{" +
                "sensor_id='" + sensor_id + '\'' +
                ", date='" + date + '\'' +
                ", leak=" + leak +
                ", time='" + time + '\'' +
                ", amplitudes=" + Arrays.toString(amplitudes) +
                '}';
    }
}
