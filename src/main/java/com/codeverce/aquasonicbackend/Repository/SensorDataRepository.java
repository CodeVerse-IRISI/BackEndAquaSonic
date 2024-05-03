package com.codeverce.aquasonicbackend.Repository;

import com.codeverce.aquasonicbackend.Model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends MongoRepository<SensorData, String> {
    @Query("{ 'sensor_id' : ?0, 'date' : ?1 }")
    List<SensorData> findBySensor_idAndDate(String sensor_id, String date);

    @Query("{ 'sensor_id' : ?0 }")
    List<SensorData> findBySensorId(String sensorId);

    List<SensorData> findAll();
}
