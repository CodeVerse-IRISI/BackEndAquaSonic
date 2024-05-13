package com.codeverce.aquasonicbackend.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

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
}
