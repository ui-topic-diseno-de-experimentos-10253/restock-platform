package com.restock.platform.resource.interfaces.rest.resources;

import java.time.LocalDate;

public record InventoryPushNotificationResource(
        Long userId,
        Long customSupplyId,
        Long batchId,
        String supplyName,
        String notificationType,
        String title,
        String body,
        Double currentStock,
        Double minStock,
        LocalDate expirationDate
) {
}
