package com.codeverce.aquasonicbackend.Repository;

import com.codeverce.aquasonicbackend.Model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReparationDataRepository extends MongoRepository<SensorData, String> {
    @Query("{ 'sensor_id' : ?0 }")
    List<SensorData> findBySensorIdOrderByDate(String sensorId);

    @Query("{ 'sensor_id' : ?0, 'date' : ?1 }")
    List<SensorData> findBySensorIdAndDateOrderByDate(String sensorId, String date);
}

