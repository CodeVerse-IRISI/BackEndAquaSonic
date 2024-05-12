package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.DTO.SensorDataDTO;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Service.SensorService;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
=======
import org.springframework.web.bind.annotation.*;

import java.util.List;

>>>>>>> 756c04ebadff9c2eac12169af9b3708ada3657cb
@RestController
@RequestMapping("/api/AquaSonic")
public class SensorController {


    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

<<<<<<< HEAD
=======

    @GetMapping("/SensorDta/{sensor_id}")
    public List<SensorData> getListDataySensorId(@PathVariable String sensor_id) {
        return sensorService.getSensorDataBySensorId(sensor_id);
    }

>>>>>>> 756c04ebadff9c2eac12169af9b3708ada3657cb
    @GetMapping("/SensorDataForToday/{sensor_id}")
    public List<SensorDataDTO> GetSensorDataForToday(@PathVariable String sensor_id) {
        return sensorService.getSensorDataForToday(sensor_id);
    }
    @GetMapping("/rateLeak/{sensor_id}")
    public double getSensorRateLeak(@PathVariable String sensor_id){
        return sensorService.calculateRateForToday(sensor_id);
    }
<<<<<<< HEAD
    @GetMapping("/SeriousDegreeLeak/{sensor_id}")
    public double GetSensorGravityRate(@PathVariable String sensor_id){
        return sensorService.calculateSensorLeakGravity(sensor_id);
    }
    @GetMapping("/AllSensorsDegreeLeak")
    public ResponseEntity<Map<String, Double>> GetAllSensorsGravityLeak(){
        Map<String, Double> sensorsGravity = sensorService.AllSensorDegreeGravity();
        return ResponseEntity.ok(sensorsGravity);
    }

    @GetMapping("/GetSensorDataFortwoDays/{sensor_id}")
    public List<SensorData> GetSensorDataFortwoDays(@PathVariable String sensor_id){
        return sensorService.getSensorDataForLastTwoDays(sensor_id);
    }
    @GetMapping("/GetAllSensorsDataFortwoDays")
    public Map<String, List<SensorData>> GetAllSensorsDataFortwoDays(){
        return  sensorService.getAllSensorsDataForLastTwoDays();
    }

}
=======

}
>>>>>>> 756c04ebadff9c2eac12169af9b3708ada3657cb
