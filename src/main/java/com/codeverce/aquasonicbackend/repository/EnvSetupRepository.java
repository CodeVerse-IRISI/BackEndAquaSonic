package com.codeverce.aquasonicbackend.repository;

import com.codeverce.aquasonicbackend.entity.EnvSetup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnvSetupRepository extends MongoRepository<EnvSetup, String> {
}
