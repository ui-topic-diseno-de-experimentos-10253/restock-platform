package com.restock.platform.resource.domain.model.entities;

import com.restock.platform.resource.domain.model.valueobjects.InventoryNotificationStatus;
import com.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "inventory_notification_logs")
public class InventoryNotificationLog extends AuditableModel {
    private Long userId;
    private Long pushDeviceTokenId;
    private Long batchId;
    private Long customSupplyId;
    private String notificationType;
    private String title;
    private String body;
    private InventoryNotificationStatus status;
    private String providerMessageId;
    private String errorMessage;

    protected InventoryNotificationLog() {
    }

    public InventoryNotificationLog(Long userId, Long pushDeviceTokenId, Long batchId, Long customSupplyId,
                                    String notificationType, String title, String body,
                                    InventoryNotificationStatus status, String providerMessageId, String errorMessage) {
        this.userId = userId;
        this.pushDeviceTokenId = pushDeviceTokenId;
        this.batchId = batchId;
        this.customSupplyId = customSupplyId;
        this.notificationType = notificationType;
        this.title = title;
        this.body = body;
        this.status = status;
        this.providerMessageId = providerMessageId;
        this.errorMessage = errorMessage;
    }
}
