package com.codeverce.aquasonicbackend.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

<<<<<<< HEAD
import java.util.List;

@Document(collection = "messages") // Specifies the MongoDB collection to which this document belongs
@Data // Lombok annotation to automatically generate getter, setter, toString, equals, and hashCode methods
public class SensorData {
    @Id // Indicates that the id field will be used as the primary identifier for MongoDB documents
    private String id;
    private String sensor_id; // Identifier for the sensor
    private String date; // Date of the sensor reading
    private int leak; // Indicator for leak status
    private String time; // Time of the sensor reading
    private double[] amplitudes; // Array of amplitudes recorded by the sensor

    // Getter methods for leak and date fields
    public int getLeak() {
        return this.leak;
    }



    public double[] getAmplitudes() {
        return this.amplitudes;
    }


    public String getDate() {
        return this.date;
    }

    // Getter method for sensor_id field
    public String getSensor_id() {
        return this.sensor_id;
    }
}
=======
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
>>>>>>> 756c04ebadff9c2eac12169af9b3708ada3657cb
