package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.Service.GraviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/api")


public class GraviteController {

    @Autowired
    private GraviteService graviteService;


    @GetMapping("/sensor/{sensorId}/amplitudes")
    public List<List<Double>> getLastAmplitudesForSensor(@PathVariable String sensorId) {
        List<List<Double>> lastAmplitudes = graviteService.getLastAmplitudesForSensor(sensorId);
        if (lastAmplitudes == null || lastAmplitudes.isEmpty()) {
            return Collections.emptyList();
        } else {
            return lastAmplitudes;
        }
    }
}


