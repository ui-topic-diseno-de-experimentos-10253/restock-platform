package com.restock.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.core.env.Environment;

@EnableMongoAuditing
@SpringBootApplication
public class RestockPlatformApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestockPlatformApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RestockPlatformApplication.class, args);
    }

    @Bean
    CommandLineRunner logEffectiveMongoConfiguration(Environment environment) {
        return args -> {
            var activeProfiles = environment.getActiveProfiles();
            var mongoUri = environment.getProperty("spring.data.mongodb.uri");
            var mongoDb = environment.getProperty("spring.data.mongodb.database");
            var rawMongoUriEnv = System.getenv("MONGODB_URI");

            LOGGER.info("Active profiles: {}", String.join(",", activeProfiles));
            LOGGER.info("Mongo database: {}", mongoDb);
            LOGGER.info("Mongo URI (spring.data.mongodb.uri): {}", maskMongoUri(mongoUri));
            LOGGER.info("Mongo URI env (MONGODB_URI): {}", maskMongoUri(rawMongoUriEnv));
        };
    }

    private static String maskMongoUri(String uri) {
        if (uri == null || uri.isBlank()) return "<empty>";
        // Mask credentials in URI: scheme://user:pass@host -> scheme://***:***@host
        return uri.replaceAll("(?i)^(mongodb(?:\\+srv)?://)([^@/]+)@", "$1***:***@");
    }
}
