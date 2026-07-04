package com.restock.platform.resource.application.internal.outboundedservices.push;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.restock.platform.iam.domain.model.entities.PushDeviceToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FirebasePushNotificationService {

    @Value("${notifications.push.enabled:true}")
    private boolean enabled;

    public String send(PushDeviceToken deviceToken, String title, String body, Map<String, Object> data) throws Exception {
        if (!enabled || FirebaseApp.getApps().isEmpty()) {
            throw new IllegalStateException("Firebase push notifications are not configured");
        }

        var message = Message.builder()
                .setToken(deviceToken.getPushToken())
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putAllData(stringifyData(data))
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }

    private Map<String, String> stringifyData(Map<String, Object> data) {
        return data.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() == null ? "" : entry.getValue().toString()
                ));
    }
}
