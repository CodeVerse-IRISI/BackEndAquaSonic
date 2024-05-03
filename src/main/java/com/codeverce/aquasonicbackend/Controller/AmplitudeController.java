package com.codeverce.aquasonicbackend.Controller;
import com.codeverce.aquasonicbackend.Service.AmplitudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/amplitudes")
public class AmplitudeController {

    @Autowired
    private AmplitudeService amplitudeService;

    @GetMapping("/{sensorId}/firstDay")
    public List<double[]> getAmplitudesForSensorOnFirstDay(@PathVariable String sensorId) {
        LocalDate firstDay = amplitudeService.getFirstDayForSensor(sensorId);
        if (firstDay == null) {
            return new ArrayList<>();
        }
        return amplitudeService.getAmplitudesForSensorOnDay(sensorId, firstDay);
    }

    @GetMapping("/{sensorId}/secondDay")
    public List<double[]> getAmplitudesForSensorOnSecondDay(@PathVariable String sensorId) {
        LocalDate secondDay = amplitudeService.getSecondDayForSensor(sensorId);
        if (secondDay == null) {
            return new ArrayList<>();
        }
        return amplitudeService.getAmplitudesForSensorOnDay(sensorId, secondDay);
    }
}
