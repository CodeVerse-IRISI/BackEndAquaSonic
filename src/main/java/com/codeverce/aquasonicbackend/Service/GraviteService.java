package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.GraviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GraviteService {

    public String calculateSeverity(List<List<Double>> amplitudeLists) {

        // Calculer l'écart type
        double stdDeviation = 0;

        String severity;
        if (stdDeviation < 0.1) {
            severity = "F8FF06";
        } else if (stdDeviation < 0.5) {
            severity = "06FF16";
        } else {
            severity = "FF0606";
        }

        return severity;
    }

}
