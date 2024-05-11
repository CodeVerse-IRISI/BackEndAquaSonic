package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.Service.SensorTauxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/Couleur")
public class SensorTauxController {

    @Autowired
    private SensorTauxService sensorTauxService;

    @GetMapping("/leakStatus")
    public ResponseEntity<Map<String, Double>> getAllLeakStatus() {
        return sensorTauxService.getAllLeakStatus();
    }

    @GetMapping("/nb_fuite/{sensor_id}")
    public double getLeakPercentageForSensor(@PathVariable("sensor_id") String sensorId) {
        return sensorTauxService.getLeaknb_sensourForSensor(sensorId);
    }
}
