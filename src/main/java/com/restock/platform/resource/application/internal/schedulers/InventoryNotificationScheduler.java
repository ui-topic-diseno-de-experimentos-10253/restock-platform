package com.restock.platform.resource.application.internal.schedulers;

import com.restock.platform.iam.infrastructure.persistence.mongodb.repositories.UserRepository;
import com.restock.platform.resource.application.internal.commandservices.InventoryPushNotificationDispatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InventoryNotificationScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryNotificationScheduler.class);

    private final UserRepository userRepository;
    private final InventoryPushNotificationDispatchService dispatchService;

    public InventoryNotificationScheduler(UserRepository userRepository,
                                          InventoryPushNotificationDispatchService dispatchService) {
        this.userRepository = userRepository;
        this.dispatchService = dispatchService;
    }

    /**
     * Runs every day at 8:00 AM.
     * Sweeps all registered users and dispatches push notifications for low stock/expiration candidates.
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailyInventoryAlerts() {
        LOGGER.info("Starting scheduled daily inventory notification dispatch...");
        try {
            var users = userRepository.findAll();
            int processed = 0;
            for (var user : users) {
                if (user.getId() != null) {
                    var result = dispatchService.dispatch(user.getId());
                    if (result.candidates() > 0) {
                        LOGGER.info("Dispatched alerts to user ID {}: {} candidates, {} active tokens, {} sent, {} failed",
                                user.getId(), result.candidates(), result.activeTokens(), result.sent(), result.failed());
                    }
                    processed++;
                }
            }
            LOGGER.info("Scheduled daily inventory notification sweep completed. Processed {} users.", processed);
        } catch (Exception e) {
            LOGGER.error("Error executing scheduled daily inventory notification sweep: {}", e.getMessage(), e);
        }
    }
}
