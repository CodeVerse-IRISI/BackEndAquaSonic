package com.codeverce.aquasonicbackend.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "rapport")
@Data
public class  CarteData {
    public String getId() {
        return id;
    }

    @Id
    private String id;

    public String getSensor_id() {
        return sensor_id;
    }

    private String sensor_id; // Identifier for the sensor

    public double getX() {
        return X;
    }

    private  double X ;

    public double getY() {
        return Y;
    }

    private  double Y ;



     String droite_id ;

    String gauche_id ;


     int nb_fuite ;

      int  nb_reparation ;


}