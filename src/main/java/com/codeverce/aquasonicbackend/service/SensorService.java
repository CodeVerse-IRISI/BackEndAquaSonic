package com.codeverce.aquasonicbackend.service;

import com.codeverce.aquasonicbackend.dto.SensorDataDTO;
import com.codeverce.aquasonicbackend.entity.CarteData;
import com.codeverce.aquasonicbackend.entity.SensorData;
import com.codeverce.aquasonicbackend.repository.CarteRepository;
import com.codeverce.aquasonicbackend.repository.SensorDataRepository;
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

/**
 * Service pour la gestion des données des capteurs.
 */
@Service
public class SensorService {
    @Autowired
    private SensorDataRepository sensorDataRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CarteRepository carteRepository;

    /**
     * Récupère les données des capteurs pour aujourd'hui.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return Liste des données des capteurs sous forme de DTO.
     */
    public List<SensorDataDTO> getSensorData(String sensor_id) {
        Date dateFromDB = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(dateFromDB);
        List<SensorData> sensorDataList = sensorDataRepository.findBySensor_idAndDate(sensor_id, formattedDate);
        return sensorDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les données des capteurs pour une date spécifique.
     *
     * @param sensor_id L'identifiant du capteur.
     * @param date La date pour laquelle récupérer les données.
     * @return Liste des données des capteurs sous forme de DTO.
     */
    public List<SensorDataDTO> getSensorData(String sensor_id, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        List<SensorData> sensorDataList = sensorDataRepository.findBySensor_idAndDate(sensor_id, formattedDate);
        return sensorDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les données des capteurs pour les n derniers jours.
     *
     * @param sensor_id L'identifiant du capteur.
     * @param numberOfDays Le nombre de jours pour lesquels récupérer les données.
     * @return Liste des données des capteurs.
     */
    public List<SensorData> getSensorData(String sensor_id, int numberOfDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate twoDaysAgo = currentDate.minusDays(numberOfDays);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = twoDaysAgo.format(formatter);
        String endDateString = currentDate.format(formatter);
        return sensorDataRepository.findByDateBetweenAndSensorId(sensor_id, startDateString, endDateString);
    }

    /**
     * Calcule le taux de fuite pour aujourd'hui.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return Le taux de fuite en pourcentage.
     */
    public double calculateRate(String sensor_id) {
        List<SensorDataDTO> sensorDataList = getSensorData(sensor_id);
        int totalCalls = sensorDataList.size();
        long leakCalls = sensorDataList.stream()
                .filter(data -> data.getLeak() == 1)
                .count();
        return totalCalls != 0 ? (double) leakCalls / totalCalls * 100 : 0;
    }

    /**
     * Calcule le taux de fuite pour une date spécifique.
     *
     * @param sensor_id L'identifiant du capteur.
     * @param date La date pour laquelle calculer le taux de fuite.
     * @return Le taux de fuite en pourcentage.
     */
    public double calculateRate(String sensor_id, Date date) {
        List<SensorDataDTO> sensorDataList = getSensorData(sensor_id, date);
        int totalCalls = sensorDataList.size();
        long leakCalls = sensorDataList.stream()
                .filter(data -> data.getLeak() == 1)
                .count();
        if (totalCalls != 0) {
            double leakRate = (double) leakCalls / totalCalls * 100;
            System.out.printf("taux :" + leakRate);
            return leakRate;
        } else {
            return 0;
        }
    }

    /**
     * Calcule la gravité de la fuite pour un capteur spécifique pour aujourd'hui.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return La gravité de la fuite en pourcentage.
     */
    public double calculateSensorLeakGravity(String sensor_id) {
        int leakRate = (int) calculateRate(sensor_id);
        List<SensorDataDTO> sensorDataList = getSensorData(sensor_id);
        double threshold = -5;
        int count = 0;
        if (leakRate == 100) {
            if (!sensorDataList.isEmpty()) {
                for (SensorDataDTO appel : sensorDataList) {
                    double averageTensions = calculateAverageTension(appel.getAmplitudes());
                    if (averageTensions > threshold) {
                        count++;
                    }
                }
                double gravityRate = ((double) count / sensorDataList.size()) * 100;
                return gravityRate;
            }
        }
        return 0;
    }

    /**
     * Méthode pour calculer la moyenne des tensions d'un appel.
     *
     * @param tensions Tableau des tensions.
     * @return La moyenne des tensions.
     */
    private double calculateAverageTension(double[] tensions) {
        double sum = 0.0;
        for (double tension : tensions) {
            sum += tension;
        }
        return sum / tensions.length;
    }

    /**
     * Retourne une map contenant la gravité de fuite pour tous les capteurs.
     *
     * @return Map des identifiants des capteurs et leur gravité de fuite.
     */
    public Map<String, Double> AllSensorDegreeGravity() {
        List<SensorData> AllSensor = sensorDataRepository.findAll();
        Map<String, Double> SensorsGravity = new HashMap<>();
        for (SensorData sensor : AllSensor) {
            double gravity = calculateSensorLeakGravity(sensor.getSensor_id());
            SensorsGravity.put(sensor.getSensor_id(), gravity);
        }
        return SensorsGravity;
    }

    /**
     * Retourne une map contenant le taux de fuite pour tous les capteurs.
     *
     * @return Map des identifiants des capteurs et leur taux de fuite.
     */
    public Map<String, Double> AllSensorsRateLeak() {
        List<SensorData> AllSensor = sensorDataRepository.findAll();
        Map<String, Double> SensorsRateLeak = new HashMap<>();
        for (SensorData sensor : AllSensor) {
            double rate = calculateRate(sensor.getSensor_id());
            SensorsRateLeak.put(sensor.getSensor_id(), rate);
        }
        return SensorsRateLeak;
    }

    /**
     * Méthode pour mettre à jour le nombre de fuites.
     *
     * @param sensor_id L'identifiant du capteur.
     * @param date La date de la dernière fuite.
     */
    public void detectLeakAndUpdateCount(String sensor_id, String date) {
        CarteData sensor = carteRepository.findBySensorId(sensor_id);
        sensor.setDateLastFuite(date);
        int leakRate = (int) calculateRate(sensor_id);
        if (leakRate >= 50) {
            if (sensor != null) {
                sensor.setNb_fuite(sensor.getNb_fuite() + 1);
            } else {
                System.out.println("Capteur non trouvé : " + sensor_id);
            }
        }
        carteRepository.save(sensor);
    }

    /**
     * Récupère toutes les données des capteurs pour un capteur spécifique.
     *
     * @param sensor_id L'identifiant du capteur.
     * @return Liste des données des capteurs.
     */
    public List<SensorData> getAllSensorData(String sensor_id) {
        return sensorDataRepository.findBySensorId(sensor_id);
    }

    /**
     * Convertit un objet SensorData en SensorDataDTO.
     *
     * @param sensorData L'objet SensorData à convertir.
     * @return L'objet SensorDataDTO converti.
     */
    private SensorDataDTO convertToDTO(SensorData sensorData) {
        return modelMapper.map(sensorData, SensorDataDTO.class);
    }
}
