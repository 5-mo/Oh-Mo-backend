package com.example.ohmobackend.service;

import com.example.ohmobackend.domain.enums.FcmNotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * FCM 알림은 firebase-service-account.json 미설정으로 비활성화 상태.
 * Firebase Admin SDK 의존성 자체를 제거해 메모리를 아끼기 위해 호출부는 그대로 두고 내부만 no-op으로 둠.
 */
@Slf4j
@Service
public class FcmService {

    public void sendNotification(String fcmToken, String title, String body, FcmNotificationType type) {
        log.debug("FCM disabled. Skipping notification to token {}", fcmToken);
    }

    @Async
    public void sendInvitationNotification(String fcmToken, long groupId, String groupName, Long invitationId) {
        log.debug("FCM disabled. Skipping invitation notification to token {}", fcmToken);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleFcmNotificationEvent(FcmNotificationEvent event) {
        sendNotifications(event.getTokens(), event.getTitle(), event.getBody(), event.getType());
    }

    @Async
    public void sendNotifications(List<String> fcmTokens, String title, String body, FcmNotificationType type) {
        log.debug("FCM disabled. Skipping {} notification(s).", fcmTokens == null ? 0 : fcmTokens.size());
    }
}
