package com.codeverce.aquasonicbackend.Controller;

import com.codeverce.aquasonicbackend.Service.ReparationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReparationDataController {
    @Autowired
    private ReparationDataService reparationDataService;

    @GetMapping("/checkReparation/{sensorId}")
    public void checkReparation(@PathVariable String sensorId) {}
}


