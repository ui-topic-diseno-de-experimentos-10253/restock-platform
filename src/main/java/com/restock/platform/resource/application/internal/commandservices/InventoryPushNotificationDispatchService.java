package com.restock.platform.resource.application.internal.commandservices;

import com.restock.platform.iam.infrastructure.persistence.mongodb.repositories.PushDeviceTokenRepository;
import com.restock.platform.resource.application.internal.outboundedservices.push.FirebasePushNotificationService;
import com.restock.platform.resource.application.internal.queryservices.InventoryPushNotificationCandidateService;
import com.restock.platform.resource.domain.model.entities.InventoryNotificationLog;
import com.restock.platform.resource.domain.model.valueobjects.InventoryNotificationStatus;
import com.restock.platform.resource.infrastructure.persistence.mongodb.repositories.InventoryNotificationLogRepository;
import com.restock.platform.resource.interfaces.rest.resources.InventoryPushDispatchResource;
import com.restock.platform.shared.infrastructure.persistence.mongodb.SequenceGeneratorService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InventoryPushNotificationDispatchService {

    private final InventoryPushNotificationCandidateService candidateService;
    private final PushDeviceTokenRepository pushDeviceTokenRepository;
    private final FirebasePushNotificationService firebasePushNotificationService;
    private final InventoryNotificationLogRepository notificationLogRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public InventoryPushNotificationDispatchService(InventoryPushNotificationCandidateService candidateService,
                                                    PushDeviceTokenRepository pushDeviceTokenRepository,
                                                    FirebasePushNotificationService firebasePushNotificationService,
                                                    InventoryNotificationLogRepository notificationLogRepository,
                                                    SequenceGeneratorService sequenceGeneratorService) {
        this.candidateService = candidateService;
        this.pushDeviceTokenRepository = pushDeviceTokenRepository;
        this.firebasePushNotificationService = firebasePushNotificationService;
        this.notificationLogRepository = notificationLogRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public InventoryPushDispatchResource dispatch(Long userId) {
        var candidates = candidateService.findCandidatesByUserId(userId);
        var tokens = pushDeviceTokenRepository.findAllByUserIdAndProviderAndActiveTrue(userId, "firebase");
        var sent = 0;
        var failed = 0;

        for (var candidate : candidates) {
            for (var token : tokens) {
                try {
                    var messageId = firebasePushNotificationService.send(
                            token,
                            candidate.title(),
                            candidate.body(),
                            Map.of(
                                    "batchId", candidate.batchId(),
                                    "customSupplyId", candidate.customSupplyId(),
                                    "type", candidate.notificationType()
                            )
                    );
                    saveLog(userId, token.getId(), candidate, InventoryNotificationStatus.SENT, messageId, null);
                    sent++;
                } catch (Exception e) {
                    saveLog(userId, token.getId(), candidate, InventoryNotificationStatus.FAILED, null, e.getMessage());
                    failed++;
                }
            }
        }

        return new InventoryPushDispatchResource(userId, candidates.size(), tokens.size(), sent, failed);
    }

    private void saveLog(Long userId, Long tokenId, com.restock.platform.resource.interfaces.rest.resources.InventoryPushNotificationResource candidate,
                         InventoryNotificationStatus status, String providerMessageId, String errorMessage) {
        var log = new InventoryNotificationLog(
                userId,
                tokenId,
                candidate.batchId(),
                candidate.customSupplyId(),
                candidate.notificationType(),
                candidate.title(),
                candidate.body(),
                status,
                providerMessageId,
                errorMessage
        );
        log.setId(sequenceGeneratorService.generateSequence("inventory_notification_logs_sequence"));
        notificationLogRepository.save(log);
    }
}
