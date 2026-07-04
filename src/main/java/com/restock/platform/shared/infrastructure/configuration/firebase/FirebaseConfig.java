package com.restock.platform.shared.infrastructure.configuration.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${notifications.push.enabled:true}")
    private boolean enabled;

    @Value("${notifications.push.provider:firebase}")
    private String provider;

    @Value("${notifications.push.project-id:}")
    private String projectId;

    @Value("${notifications.push.service-account-base64:}")
    private String serviceAccountBase64;

    @PostConstruct
    public void initializeFirebase() {
        if (!enabled || !"firebase".equalsIgnoreCase(provider)
                || projectId == null || projectId.isBlank()
                || serviceAccountBase64 == null || serviceAccountBase64.isBlank()
                || !FirebaseApp.getApps().isEmpty()) {
            return;
        }

        try {
            var serviceAccountJson = Base64.getDecoder().decode(serviceAccountBase64);
            var credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(new String(serviceAccountJson, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8)));
            var options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            throw new IllegalStateException("Firebase Admin SDK could not be initialized", e);
        }
    }
}
