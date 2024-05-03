package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.Service.SensorTauxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Rapport")
public class SensorTauxController {

    @Autowired
    private SensorTauxService sensorTauxService;

    @GetMapping("/leakStatus/{sensor_id}")
    public ResponseEntity<String> getLeakStatusForSensor(@PathVariable String sensor_id) {
        ResponseEntity<String> responseEntity = sensorTauxService.getLeakStatusForSensor(sensor_id);
        return responseEntity;
    }
}

