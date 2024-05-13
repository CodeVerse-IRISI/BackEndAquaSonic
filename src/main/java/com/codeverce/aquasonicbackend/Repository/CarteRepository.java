package com.codeverce.aquasonicbackend.Repository;

import com.codeverce.aquasonicbackend.Model.CarteData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


    public interface CarteRepository extends MongoRepository<CarteData, String> {
        @Query("{ 'sensor_id' : ?0 }")
        CarteData findBySensorId(String sensor_id);

        @Query(value = "{}", fields = "{'_id': 0}")
        List<CarteRepository.CarteDataProjection> getAllCapteurs();

        interface CarteDataProjection {
            String getSensor_id();
            Double getX();
            Double getY();
        }
    }


