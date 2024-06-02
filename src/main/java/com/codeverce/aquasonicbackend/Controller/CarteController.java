package com.codeverce.aquasonicbackend.Controller;



import com.codeverce.aquasonicbackend.DTO.Capteur;
import com.codeverce.aquasonicbackend.Model.CarteData;
import com.codeverce.aquasonicbackend.Service.CarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CarteController {

    @Autowired
    private CarteService carteService;

    //api retourne tous les capteur intaller avec leurs positions
    @GetMapping("/capteurs")
    public List<Capteur> getAllCapteurs() {
        List<Capteur> capteurList = carteService.getAllCapteurs();
        return capteurList;
    }

    //api retourne les details d'un sensor
    @GetMapping("/information/{sensor_id}")
    public CarteData getSensorInformation(@PathVariable("sensor_id") String sensorId) {
        return carteService.findInformationSensorId(sensorId);
    }

}
