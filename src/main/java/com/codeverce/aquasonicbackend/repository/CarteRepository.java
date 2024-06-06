package com.codeverce.aquasonicbackend.repository;

import com.codeverce.aquasonicbackend.entity.CarteData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface CarteRepository extends MongoRepository<CarteData, String> {
    @Query("{ 'sensor_id' : ?0 }")
    CarteData findBySensorId(String sensorId);

    @Query(value = "{}", fields = "{'_id': 0}")
    List<CarteData> getAllCapteurs();

}