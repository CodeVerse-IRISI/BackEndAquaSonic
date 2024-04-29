package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.DTO.SensorDataDTO;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Service.SensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/AquaSonic")
public class SensorController {


    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }


    @GetMapping("/SensorDta/{sensor_id}")
    public List<SensorData> getListDataySensorId(@PathVariable String sensor_id) {
        return sensorService.getSensorDataBySensorId(sensor_id);
    }

    @GetMapping("/SensorDataForToday/{sensor_id}")
    public List<SensorDataDTO> GetSensorDataForToday(@PathVariable String sensor_id) {
        return sensorService.getSensorDataForToday(sensor_id);
    }
    @GetMapping("/rateLeak/{sensor_id}")
    public double getSensorRateLeak(@PathVariable String sensor_id){
        return sensorService.calculateRateForToday(sensor_id);
    }

}