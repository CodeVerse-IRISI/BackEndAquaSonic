package com.codeverce.aquasonicbackend.Service;

import com.codeverce.aquasonicbackend.DTO.Capteur;
import com.codeverce.aquasonicbackend.Model.CarteData;
import com.codeverce.aquasonicbackend.Repository.CarteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarteService {

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    public CarteService(CarteRepository carteRepository) {
        this.carteRepository = carteRepository;
    }

    @Autowired
    SensorService sensorService;


    public List<Capteur> getAllCapteurs() {
        List<CarteData> carteDataList = carteRepository.getAllCapteurs();
        return carteDataList.stream()
                .map(carteData -> new Capteur(carteData.getSensor_id(), carteData.getX(), carteData.getY()))
                .collect(Collectors.toList());
    }


    public CarteData findInformationSensorId(String sensorId) {
        CarteData carteData = carteRepository.findBySensorId(sensorId);
        return carteData;
    }

    public CarteData saveCarteData(CarteData carteData) {
        return carteRepository.save(carteData);
    }


}
