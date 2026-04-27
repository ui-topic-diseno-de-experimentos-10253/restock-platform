package com.restock.platform.resource.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateOrderResource(
        Long adminRestaurantId,
        Long supplierId,
        List<AssignedBatchResource> batches,
        String description,
        LocalDate estimatedShipDate,
        LocalTime estimatedShipTime
) {}
