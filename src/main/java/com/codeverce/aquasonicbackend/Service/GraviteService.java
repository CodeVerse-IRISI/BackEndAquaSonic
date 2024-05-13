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

    @Autowired
    private GraviteRepository graviteRepository;

    public double getTotalAmplitudeSumForSensor(String sensorId) {
        List<List<Double>> amplitudeLists = getLastAmplitudesForSensor(sensorId);
        return calculateTotalAmplitudeSum(amplitudeLists);
    }

    public List<List<Double>> getLastAmplitudesForSensor(String sensorId) {
        List<SensorData> sensorDataList = graviteRepository.findSensorDataBySensorId(sensorId);
        if (sensorDataList.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<Double>> amplitudeLists = new ArrayList<>();

        for (SensorData sensorData : sensorDataList) {
            double[] amplitudes = sensorData.getAmplitudes();
            if (amplitudes != null && amplitudes.length > 0) {
                // Convertir le tableau d'amplitudes en une liste de doubles
                List<Double> amplitudeList = new ArrayList<>();
                for (double amplitude : amplitudes) {
                    amplitudeList.add(amplitude);
                }
                // Ajouter la liste d'amplitudes à la liste de listes
                amplitudeLists.add(amplitudeList);
            }
        }

        // Calculer la somme totale à l'extérieur de la boucle for
        double totalSum = calculateTotalAmplitudeSum(amplitudeLists);
        System.out.println(totalSum); // Afficher la somme totale
        return amplitudeLists;
    }

    public double calculateTotalAmplitudeSum(List<List<Double>> amplitudeLists) {
        double totalSum = 0.0;

        // Parcourir chaque liste d'amplitudes
        for (List<Double> amplitudeList : amplitudeLists) {
            // Parcourir chaque valeur d'amplitude dans la liste
            for (Double amplitude : amplitudeList) {
                // Ajouter la valeur d'amplitude à la somme totale
                totalSum += amplitude;
                System.out.println(totalSum);
            }
        }

        return totalSum;
    }

    public String calculateSeverity(List<List<Double>> amplitudeLists) {
        double totalSum = calculateTotalAmplitudeSum(amplitudeLists);

        // Calculer la moyenne
        double mean = totalSum / amplitudeLists.size();

        // Calculer la somme des écarts au carré par rapport à la moyenne
        double sumSquaredDiff = 0.0;
        for (List<Double> amplitudeList : amplitudeLists) {
            for (Double amplitude : amplitudeList) {
                sumSquaredDiff += Math.pow(amplitude - mean, 2);
            }
        }

        // Calculer l'écart type
        double stdDeviation = Math.sqrt(sumSquaredDiff / amplitudeLists.size());

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
