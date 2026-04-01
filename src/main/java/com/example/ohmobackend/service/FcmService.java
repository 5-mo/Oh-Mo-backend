package com.example.ohmobackend.service;

import com.example.ohmobackend.domain.enums.FcmNotificationType;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Service
public class FcmService {

    public void sendNotification(String fcmToken, String title, String body, FcmNotificationType type) {
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
                    .putData("type", type.name())
                    .build();
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.warn("FCM notification failed for token {}: {}", fcmToken, e.getMessage());
        }
    }

    @Async("fcmExecutor")
    public void sendInvitationNotification(String fcmToken, long groupId, String groupName, Long invitationId) {
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
                            .setTitle("그룹에 초대됐어요")
                            .setBody(groupName + " 그룹에 초대됐습니다.")
                            .build())
                    .putData("type", FcmNotificationType.GROUP_INVITATION.name())
                    .putData("groupId", String.valueOf(groupId))
                    .putData("groupName", groupName)
                    .putData("invitationId", String.valueOf(invitationId))
                    .build();
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            log.warn("FCM invitation notification failed for token {}: {}", fcmToken, e.getMessage());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("fcmExecutor")
    public void handleFcmNotificationEvent(FcmNotificationEvent event) {
        sendNotifications(event.getTokens(), event.getTitle(), event.getBody(), event.getType());
    }

    @Async("fcmExecutor")
    public void sendNotifications(List<String> fcmTokens, String title, String body, FcmNotificationType type) {
        if (fcmTokens == null || fcmTokens.isEmpty()) {
            return;
        }
        fcmTokens.stream()
                .filter(token -> token != null && !token.isBlank())
                .forEach(token -> sendNotification(token, title, body, type));
    }
}
