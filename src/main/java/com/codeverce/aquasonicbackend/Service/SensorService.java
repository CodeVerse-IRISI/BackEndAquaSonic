package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.DTO.SensorDataDTO;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.SensorDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SensorService {
    @Autowired
    private SensorDataRepository sensorDataRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<SensorDataDTO> getSensorDataForToday(String sensor_id) {
        // Date récupérée depuis MongoDB
        Date dateFromDB = new Date();

        // Formatage de la date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(dateFromDB);

        // Affichage de la date formatée
        System.out.println("Date formatée : " + formattedDate);

        // Récupérer les données des capteurs pour aujourd'hui
        List<SensorData> sensorDataList = sensorDataRepository.findBySensor_idAndDate(sensor_id,formattedDate);

        // Mapper les SensorData en SensorDataDTO
        List<SensorDataDTO> sensorDataDTOList = sensorDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return sensorDataDTOList;
    }
  
    public double calculateRateForToday(String sensor_id) {
        // Récupérer les données des capteurs pour aujourd'hui
        List<SensorDataDTO> sensorDataList = getSensorDataForToday(sensor_id);

        // Compter le nombre total d'appels détectés aujourd'hui
        int totalCalls = sensorDataList.size();

        // Compter le nombre d'appels détectés comme une fuite aujourd'hui
        long leakCalls = sensorDataList.stream()
                .filter(data -> data.getLeak() == 1)
                .count();

        // Vérifier si le total est différent de zéro
        if (totalCalls != 0) {
            // Calculer le taux de fuite
            double leakRate = (double) leakCalls / totalCalls * 100;
            System.out.printf("taux :"+ leakRate);
            return leakRate;
        } else {
            return 0;
        }
    }

    // Méthode pour calculer la gravité de la fuite
    public double calculateSensorLeakGravity(String sensor_id) {
        int leakRate = (int) calculateRateForToday(sensor_id);
        // Récupérer les données des capteurs pour aujourd'hui
        List<SensorDataDTO> sensorDataList = getSensorDataForToday(sensor_id);
        // Comparer la moyenne avec un seuil prédéfini
        double threshold = -5; // Assurez-vous que le seuil est correctement défini
        int count = 0;

        if (leakRate == 100) {
            if (!sensorDataList.isEmpty()) {
                for (SensorDataDTO appel : sensorDataList) {
                    double averageTensions = calculateAverageTension(appel.getAmplitudes());
                    if (averageTensions > threshold) {
                        count++;
                    }
                }
                // Calculer le taux de gravité de la fuite à partir de tous les appels
                double gravityRate = ((double) count / sensorDataList.size()) * 100;
               return gravityRate;
            } else {
                System.out.println("La liste des données de capteur est vide");
            }
        } else {
            System.out.println("Le taux de réalité de fuite est < 100%");
        }
        return 0;
    }


    // Méthode pour calculer la moyenne des tensions d'un appel
    private double calculateAverageTension(double[] tensions) {
        double sum = 0.0;
        for (double tension : tensions) {
            sum += tension;
        }
        return sum / tensions.length;
    }

    public Map<String, Double> AllSensorDegreeGravity(){
        List<SensorData> AllSensor = sensorDataRepository.findAll();
        Map<String, Double> SensorsGravity = new HashMap<>();
        for (SensorData sensor:AllSensor) {
            double gravity= calculateSensorLeakGravity(sensor.getSensor_id());
            SensorsGravity.put(sensor.getSensor_id(),gravity);
        }
        return SensorsGravity;
    }

    public List<SensorData> getSensorDataForLastTwoDays(String sensor_id){
       // Date actuelle
        LocalDate currentDate = LocalDate.now();

        // Date il y a deux jours
        LocalDate twoDaysAgo = currentDate.minusDays(2);

        // Formatter pour la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convertir les dates en chaînes de caractères
        String startDateString = twoDaysAgo.format(formatter);
        String endDateString = currentDate.format(formatter);

        // Interroger la base de données pour obtenir les données entre ces deux dates
        return sensorDataRepository.findByDateBetweenAndSensorId(sensor_id,startDateString, endDateString);
    }

    public Map<String, List<SensorData>> getAllSensorsDataForLastTwoDays() {
        List<SensorData> AllSensor = sensorDataRepository.findAll();
        Map<String, List<SensorData>> MapSensorsData = new HashMap<>();
        for (SensorData sensor : AllSensor) {
            List<SensorData> sensorData = getSensorDataForLastTwoDays(sensor.getSensor_id());
            MapSensorsData.put(sensor.getSensor_id(), sensorData);
        }
        return MapSensorsData;
    }

    public List<SensorData> getSensorDataBySensorId(String sensor_id){
       return sensorDataRepository.findBySensorId(sensor_id);
    }

    private SensorDataDTO convertToDTO(SensorData sensorData) {
        return modelMapper.map(sensorData, SensorDataDTO.class);
    }

}

