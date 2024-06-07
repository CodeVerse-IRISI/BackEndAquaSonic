package com.codeverce.aquasonicbackend.repository;

import com.codeverce.aquasonicbackend.entity.SensorData;
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

    @Query("{ 'sensor_id' : ?0, 'date' : { $gte : ?1, $lte : ?2 } }")
    List<SensorData> findByDateBetweenAndSensorId(String sensor_id , String startDate  , String EndDate);
}
