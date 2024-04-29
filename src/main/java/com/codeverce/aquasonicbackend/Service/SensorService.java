package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.DTO.SensorDataDTO;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Repository.SensorDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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



    public List<SensorData> getSensorDataBySensorId(String sensor_id){
       return sensorDataRepository.findBySensorId(sensor_id);
    }

    private SensorDataDTO convertToDTO(SensorData sensorData) {
        return modelMapper.map(sensorData, SensorDataDTO.class);
    }

}