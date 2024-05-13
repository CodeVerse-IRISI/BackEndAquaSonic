package com.codeverce.aquasonicbackend.Service;


import com.codeverce.aquasonicbackend.Model.CarteData;
import com.codeverce.aquasonicbackend.Repository.CarteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service

public class CarteService {

    @Autowired
    private CarteRepository carteRepository;

    public List<CarteData> getAllCapteurs() {
        List<CarteRepository.CarteDataProjection> allCapteurs = carteRepository.getAllCapteurs();
        List<CarteData> filteredCapteurs = new ArrayList<>();

        for (CarteRepository.CarteDataProjection capteur : allCapteurs) {
            CarteData filteredCapteur = new CarteData();
            filteredCapteur.setSensor_id(capteur.getSensor_id());
            filteredCapteur.setX(capteur.getX());
            filteredCapteur.setY(capteur.getY());
            filteredCapteurs.add(filteredCapteur);
        }

        return filteredCapteurs;
    }
}




