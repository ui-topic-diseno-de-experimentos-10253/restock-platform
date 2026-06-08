package com.restock.platform.monitoring.interfaces.rest.controllers;

import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/monitoring")
public class HealthController {

    private final MongoTemplate mongoTemplate;
    private final Environment environment;

    public HealthController(MongoTemplate mongoTemplate, Environment environment) {
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        try {
            mongoTemplate.getDb().runCommand(new Document("ping", 1));

            return ResponseEntity.ok(Map.of(
                    "status", "UP",
                    "service", "restock-platform-api",
                    "timestamp", Instant.now().toString(),
                    "database", "UP",
                    "profiles", Arrays.toString(environment.getActiveProfiles())
            ));

        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "status", "DOWN",
                            "service", "restock-platform-api",
                            "timestamp", Instant.now().toString(),
                            "database", "DOWN",
                            "error", exception.getClass().getSimpleName()
                    ));
        }
    }
}