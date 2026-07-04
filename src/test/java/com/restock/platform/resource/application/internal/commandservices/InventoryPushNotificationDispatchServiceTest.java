package com.restock.platform.resource.application.internal.commandservices;

import com.restock.platform.iam.domain.model.entities.PushDeviceToken;
import com.restock.platform.iam.infrastructure.persistence.mongodb.repositories.PushDeviceTokenRepository;
import com.restock.platform.resource.application.internal.outboundedservices.push.FirebasePushNotificationService;
import com.restock.platform.resource.application.internal.queryservices.InventoryPushNotificationCandidateService;
import com.restock.platform.resource.domain.model.entities.InventoryNotificationLog;
import com.restock.platform.resource.domain.model.valueobjects.InventoryNotificationStatus;
import com.restock.platform.resource.infrastructure.persistence.mongodb.repositories.InventoryNotificationLogRepository;
import com.restock.platform.resource.interfaces.rest.resources.InventoryPushNotificationResource;
import com.restock.platform.shared.infrastructure.persistence.mongodb.SequenceGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InventoryPushNotificationDispatchServiceTest {

    @Test
    void dispatchSendsFirebasePushAndStoresSentLog() throws Exception {
        var candidateService = mock(InventoryPushNotificationCandidateService.class);
        var tokenRepository = mock(PushDeviceTokenRepository.class);
        var firebaseService = mock(FirebasePushNotificationService.class);
        var logRepository = mock(InventoryNotificationLogRepository.class);
        var sequenceGeneratorService = mock(SequenceGeneratorService.class);
        var service = new InventoryPushNotificationDispatchService(
                candidateService,
                tokenRepository,
                firebaseService,
                logRepository,
                sequenceGeneratorService
        );

        var userId = 1L;
        var candidate = new InventoryPushNotificationResource(
                userId,
                20L,
                30L,
                "Tomate",
                "MIN_STOCK",
                "Stock minimo alcanzado",
                "Tomate alcanzo su stock minimo.",
                2.0,
                5.0,
                LocalDate.now().plusDays(2)
        );
        var token = new PushDeviceToken(userId, "android", "firebase", "fcm-token");
        token.setId(99L);

        when(candidateService.findCandidatesByUserId(userId)).thenReturn(List.of(candidate));
        when(tokenRepository.findAllByUserIdAndProviderAndActiveTrue(userId, "firebase")).thenReturn(List.of(token));
        when(firebaseService.send(eq(token), eq(candidate.title()), eq(candidate.body()), any())).thenReturn("firebase-message-id");
        when(sequenceGeneratorService.generateSequence("inventory_notification_logs_sequence")).thenReturn(100L);

        var result = service.dispatch(userId);

        assertThat(result.sent()).isEqualTo(1);
        assertThat(result.failed()).isZero();

        var logCaptor = ArgumentCaptor.forClass(InventoryNotificationLog.class);
        verify(logRepository).save(logCaptor.capture());
        assertThat(logCaptor.getValue().getStatus()).isEqualTo(InventoryNotificationStatus.SENT);
        assertThat(logCaptor.getValue().getProviderMessageId()).isEqualTo("firebase-message-id");
    }
}
