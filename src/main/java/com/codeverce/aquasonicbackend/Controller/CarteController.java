package com.codeverce.aquasonicbackend.Controller;



import com.codeverce.aquasonicbackend.Model.CarteData;
import com.codeverce.aquasonicbackend.Service.CarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarteController {

    @Autowired
    private CarteService carteService;

    @GetMapping("/capteurs")
    public List<CarteData> getAllCapteurs() {
        return carteService.getAllCapteurs();
    }
}
