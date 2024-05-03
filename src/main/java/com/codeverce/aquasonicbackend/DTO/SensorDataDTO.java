package com.codeverce.aquasonicbackend.DTO;

import lombok.Data;


@Data
public class SensorDataDTO {
    private String date;
    private int leak;
    private String time;
    private double[] amplitudes;
    public int getLeak() {
        return this.leak;
    }


}
