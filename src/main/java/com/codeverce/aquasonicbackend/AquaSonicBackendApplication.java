package com.codeverce.aquasonicbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application AquaSonicBackend.
 */
@SpringBootApplication
@OpenAPIDefinition
public class AquaSonicBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AquaSonicBackendApplication.class, args);
    }

}
