package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.DTO.SensorDataDTO;
import com.codeverce.aquasonicbackend.Model.SensorData;
import com.codeverce.aquasonicbackend.Service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/AquaSonic")
public class SensorController {


    private final SensorService sensorService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/SensorData/{sensor_id}")
    public List<SensorData> getListDataBySensorId(@PathVariable String sensor_id) {
        return sensorService.getAllSensorData(sensor_id);
    }

    @GetMapping("/SensorDataByDate/{sensor_id}/{dateStr}")
    public List<SensorDataDTO> getListData(@PathVariable String sensor_id,@PathVariable String dateStr) {
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sensorService.getSensorData(sensor_id, date);
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
    @GetMapping("/Couleur/leakStatus")
    public ResponseEntity<Map<String, Double>> GetAllSensorsRateLeak(){
        Map<String, Double> sensorsGravity = sensorService.AllSensorsRateLeak();
        return ResponseEntity.ok(sensorsGravity);
    }

    @GetMapping("/GetSensorDataForNDays/{sensor_id}/{numberOfDays}")
    public List<SensorData> GetSensorDataForNDays(@PathVariable("sensor_id") String sensorId, @PathVariable("numberOfDays") int numberOfDays) {
        return sensorService.getSensorData(sensorId, numberOfDays);
    }



}
