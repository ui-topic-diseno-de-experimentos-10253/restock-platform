package com.restock.platform.resource.application.internal.queryservices;

import com.restock.platform.resource.domain.model.aggregates.Batch;
import com.restock.platform.resource.domain.model.aggregates.CustomSupply;
import com.restock.platform.resource.domain.model.queries.GetBatchesByUserIdQuery;
import com.restock.platform.resource.domain.services.BatchQueryService;
import com.restock.platform.resource.interfaces.rest.resources.InventoryPushNotificationResource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryPushNotificationCandidateService {

    private final BatchQueryService batchQueryService;

    public InventoryPushNotificationCandidateService(BatchQueryService batchQueryService) {
        this.batchQueryService = batchQueryService;
    }

    public List<InventoryPushNotificationResource> findCandidatesByUserId(Long userId) {
        var today = LocalDate.now();
        var limit = today.plusDays(5);
        return batchQueryService.handle(new GetBatchesByUserIdQuery(userId)).stream()
                .flatMap(batch -> buildNotifications(batch, today, limit).stream())
                .toList();
    }

    private List<InventoryPushNotificationResource> buildNotifications(Batch batch, LocalDate today, LocalDate limit) {
        var customSupply = batch.getCustomSupply();
        if (customSupply == null || customSupply.getStockRange() == null) return List.of();

        var notifications = new ArrayList<InventoryPushNotificationResource>();
        var stock = batch.getStock() == null ? 0.0 : batch.getStock();
        var minStock = customSupply.getStockRange().minStock();
        var supplyName = resolveSupplyName(customSupply);

        if (stock <= minStock) {
            notifications.add(new InventoryPushNotificationResource(
                    batch.getUserId(),
                    batch.getCustomSupplyId(),
                    batch.getId(),
                    supplyName,
                    "MIN_STOCK",
                    "Stock minimo alcanzado",
                    supplyName + " alcanzo su stock minimo.",
                    stock,
                    minStock,
                    batch.getExpirationDate()
            ));
        }

        if (batch.getExpirationDate() != null
                && !batch.getExpirationDate().isBefore(today)
                && !batch.getExpirationDate().isAfter(limit)) {
            notifications.add(new InventoryPushNotificationResource(
                    batch.getUserId(),
                    batch.getCustomSupplyId(),
                    batch.getId(),
                    supplyName,
                    "EXPIRATION_SOON",
                    "Insumo por vencer",
                    supplyName + " vence en los proximos 5 dias.",
                    stock,
                    minStock,
                    batch.getExpirationDate()
            ));
        }

        return notifications;
    }

    private String resolveSupplyName(CustomSupply customSupply) {
        if (customSupply.getSupply() != null && customSupply.getSupply().getName() != null) {
            return customSupply.getSupply().getName();
        }
        return customSupply.getDescription();
    }
}
