package com.codeverce.aquasonicbackend.DTO;

import lombok.Data;


@Data
public class SensorDataDTO {
    private String date;
    private int leak;
    private String time;
    private double[] amplitudes;
<<<<<<< HEAD
    public int getLeak() {
        return this.leak;
    }
=======
>>>>>>> 756c04ebadff9c2eac12169af9b3708ada3657cb


}
