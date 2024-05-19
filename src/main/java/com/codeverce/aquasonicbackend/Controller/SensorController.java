package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.DTO.SensorDataDTO;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/AquaSonic")
public class SensorController {


    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/SensorData/{sensor_id}")
    public List<SensorData> getListDataBySensorId(@PathVariable String sensor_id) {
        return sensorService.getAllSensorData(sensor_id);
    }

    @GetMapping("/SensorDataForToday/{sensor_id}")
    public List<SensorDataDTO> GetSensorDataForToday(@PathVariable String sensor_id) {
        return sensorService.getSensorData(sensor_id);
    }
    @GetMapping("/rateLeak/{sensor_id}")
    public double getSensorRateLeak(@PathVariable String sensor_id){
        return sensorService.calculateRate(sensor_id);
    }

    @GetMapping("/SeriousDegreeLeak/{sensor_id}")
    public double GetSensorGravityRate(@PathVariable String sensor_id){
        return sensorService.calculateSensorLeakGravity(sensor_id);
    }
    @GetMapping("/AllSensorsDegreeLeak")
    public ResponseEntity<Map<String, Double>> GetAllSensorsGravityLeak(){
        Map<String, Double> sensorsGravity = sensorService.AllSensorDegreeGravity();
        return ResponseEntity.ok(sensorsGravity);
    }

    @GetMapping("/GetSensorDataForNDays/{sensor_id}/{numberOfDays}")
    public List<SensorData> GetSensorDataForNDays(@PathVariable("sensor_id") String sensorId, @PathVariable("numberOfDays") int numberOfDays) {
        return sensorService.getSensorData(sensorId, numberOfDays);
    }

    @GetMapping("/GetSensorData/{sensor_id}/{date}")
    public List<SensorDataDTO> GetSensorData(@PathVariable("sensor_id") String sensorId, @PathVariable("date") Date date){
       return sensorService.getSensorData(sensorId,date);
    }

}
