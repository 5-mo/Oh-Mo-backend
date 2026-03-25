package com.example.ohmobackend.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FcmService {

    public void sendNotification(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isBlank()) {
            return;
        }
        if (FirebaseApp.getApps().isEmpty()) {
            log.warn("Firebase not initialized. Skipping FCM notification.");
            return;
        }
        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.warn("FCM notification failed for token {}: {}", fcmToken, e.getMessage());
        }
    }

    public void sendNotifications(List<String> fcmTokens, String title, String body) {
        if (fcmTokens == null || fcmTokens.isEmpty()) {
            return;
        }
        fcmTokens.stream()
                .filter(token -> token != null && !token.isBlank())
                .forEach(token -> sendNotification(token, title, body));
    }
}
