package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.Service.SensorRapportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Rapport")
public class SensorRapportController {

    @Autowired
    private SensorRapportService sensorRapportService;

    @GetMapping("/sensor/{sensor_id}")
    public Map<String, Map<String, List<String>>> getSensorDataForToday(@PathVariable String sensor_id) {
        ResponseEntity<Map<String, Map<String, List<String>>>> responseEntity = sensorRapportService.getSensorRapportForDate(sensor_id);
        return responseEntity.getBody();
    }
}

