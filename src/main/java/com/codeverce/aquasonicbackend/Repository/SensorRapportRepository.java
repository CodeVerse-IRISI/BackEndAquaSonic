package com.codeverce.aquasonicbackend.Repository;

import com.codeverce.aquasonicbackend.Model.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

        @Repository
        public interface SensorRapportRepository extends MongoRepository<SensorData, String> {

                @Query("{ 'sensor_id' : ?0, 'date' : { $gte : ?1, $lte : ?2 } }")
                List<SensorData> findBySensorIdAndDateBetween(String sensor_id, String startDate, String endDate);

                @Query("{ 'sensor_id' : ?0 }") // Requête pour rechercher par sensor_id
            List<SensorData> findFirstBySensorIdOrderByDate(String sensor_id);
        }


