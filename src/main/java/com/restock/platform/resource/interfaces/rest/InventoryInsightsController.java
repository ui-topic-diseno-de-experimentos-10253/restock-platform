package com.restock.platform.resource.interfaces.rest;

import com.restock.platform.monitoring.domain.model.queries.GetAllSalesByUserIdQuery;
import com.restock.platform.monitoring.domain.services.SaleQueryService;
import com.restock.platform.resource.domain.model.aggregates.Batch;
import com.restock.platform.resource.domain.model.aggregates.CustomSupply;
import com.restock.platform.resource.domain.model.queries.GetBatchesByUserIdQuery;
import com.restock.platform.resource.domain.model.queries.GetCustomSuppliesByUserIdQuery;
import com.restock.platform.resource.domain.services.BatchQueryService;
import com.restock.platform.resource.domain.services.CustomSupplyQueryService;
import com.restock.platform.resource.interfaces.rest.resources.InventoryPushNotificationResource;
import com.restock.platform.resource.interfaces.rest.resources.InventoryRotationResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/inventory", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Inventory Insights", description = "Derived inventory endpoints for mobile and purchasing decisions")
public class InventoryInsightsController {

    private final BatchQueryService batchQueryService;
    private final CustomSupplyQueryService customSupplyQueryService;
    private final SaleQueryService saleQueryService;

    public InventoryInsightsController(BatchQueryService batchQueryService,
                                       CustomSupplyQueryService customSupplyQueryService,
                                       SaleQueryService saleQueryService) {
        this.batchQueryService = batchQueryService;
        this.customSupplyQueryService = customSupplyQueryService;
        this.saleQueryService = saleQueryService;
    }

    @GetMapping("/users/{userId}/push-notifications-candidates")
    @Operation(summary = "Get automatic inventory push notification candidates")
    public ResponseEntity<List<InventoryPushNotificationResource>> getPushNotifications(@PathVariable Long userId) {
        var today = LocalDate.now();
        var limit = today.plusDays(5);
        var notifications = batchQueryService.handle(new GetBatchesByUserIdQuery(userId)).stream()
                .flatMap(batch -> buildNotifications(batch, today, limit).stream())
                .toList();

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/users/{userId}/rotation")
    @Operation(summary = "Get inventory rotation level by supply")
    public ResponseEntity<List<InventoryRotationResource>> getRotationBySupply(@PathVariable Long userId) {
        var batches = batchQueryService.handle(new GetBatchesByUserIdQuery(userId));
        var sales = saleQueryService.handle(new GetAllSalesByUserIdQuery(userId.intValue()));
        var resources = customSupplyQueryService.handle(new GetCustomSuppliesByUserIdQuery(userId)).stream()
                .map(customSupply -> {
                    var consumedUnits = sales.stream()
                            .flatMap(sale -> sale.getSupplySelections().stream())
                            .filter(selection -> Objects.equals(selection.getSupplyId(), customSupply.getSupplyId()))
                            .mapToInt(selection -> selection.getQuantity() == null ? 0 : selection.getQuantity())
                            .sum();
                    var currentStock = batches.stream()
                            .filter(batch -> Objects.equals(batch.getCustomSupplyId(), customSupply.getId()))
                            .mapToDouble(batch -> batch.getStock() == null ? 0.0 : batch.getStock())
                            .sum();
                    return new InventoryRotationResource(
                            customSupply.getId(),
                            customSupply.getSupplyId(),
                            resolveSupplyName(customSupply),
                            resolveRotationLevel(consumedUnits),
                            consumedUnits,
                            currentStock
                    );
                })
                .toList();

        return ResponseEntity.ok(resources);
    }

    private List<InventoryPushNotificationResource> buildNotifications(Batch batch, LocalDate today, LocalDate limit) {
        var customSupply = batch.getCustomSupply();
        if (customSupply == null || customSupply.getStockRange() == null) return List.of();

        var notifications = new java.util.ArrayList<InventoryPushNotificationResource>();
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

    private String resolveRotationLevel(int consumedUnits) {
        if (consumedUnits >= 20) return "Alta";
        if (consumedUnits >= 5) return "Media";
        return "Baja";
    }
}
