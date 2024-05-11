package com.codeverce.aquasonicbackend.Repository;

import com.codeverce.aquasonicbackend.Model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GraviteRepository extends MongoRepository<SensorData, String> {
    @Query(value = "{'sensor_id': ?0}", fields = "{'amplitudes': 1, '_id': 0}")
    List<SensorData> findSensorDataBySensorId(String sensorId);

    @Query(value = "{'sensor_id': ?0, 'date': ?1}", fields = "{'amplitudes': 1, '_id': 0}")
    List<SensorData> findSensorDataBySensorIdAndDate(String sensorId, LocalDate date);
}

