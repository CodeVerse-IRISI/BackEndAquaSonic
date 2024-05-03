package com.codeverce.aquasonicbackend.Repository;

import com.codeverce.aquasonicbackend.Model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AmplitudeRepository extends MongoRepository<SensorData, String> {

    @Query("{'sensor_id': ?0}")
    List<SensorData> findSensorDataBySensorId(String sensorId);
}

