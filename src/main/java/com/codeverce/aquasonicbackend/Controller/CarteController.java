package com.codeverce.aquasonicbackend.Controller;



import com.codeverce.aquasonicbackend.DTO.Capteur;
import com.codeverce.aquasonicbackend.Service.CarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CarteController {

    @Autowired
    private CarteService carteService;



    @GetMapping("/capteurs")
    public List<Capteur> getAllCapteurs() {
        List<Capteur> capteurList = carteService.getAllCapteurs().stream()
                .map(carteData -> new Capteur(carteData.getSensor_id(), carteData.getX(), carteData.getY()))

                .collect(Collectors.toList());
        return capteurList;
    }


    @GetMapping("/information/{sensor_id}")
    public Map<String, Object> getSensorInformation(@PathVariable("sensor_id") String sensorId) {
        return carteService.findInformationSensorIdWithAdditionalField(sensorId);
    }

}
