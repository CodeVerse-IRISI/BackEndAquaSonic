package com.codeverce.aquasonicbackend.Repository;
import org.springframework.data.domain.Pageable;
import com.codeverce.aquasonicbackend.Model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository

public interface SensorTauxRepository extends MongoRepository<SensorData, String> {
    @Query("{ 'sensor_id' : ?0 }")

    List<SensorData> findAllBySensorIdOrderByDateAsc(String sensor_id, Pageable pageable);
    @Query(value = "{}", fields = "{'sensor_id': 1}")
    List<SensorIdProjection> findAllSensorIds();

    interface SensorIdProjection {
        String getSensor_id();
    }

}


