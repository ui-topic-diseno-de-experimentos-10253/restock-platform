package com.restock.platform.resource.interfaces.rest.resources;

import java.time.LocalDate;

/**
 * Resource to receive data for creating a new Batch.
 */
public record CreateBatchResource(
        Long userId,
        Long customSupplyId,
        Integer stock,
        LocalDate expirationDate
) {
    public CreateBatchResource {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("User ID must be a positive number");
        if (customSupplyId == null || customSupplyId <= 0)
            throw new IllegalArgumentException("Supply ID must be a positive number");
        if (stock < 0)
            throw new IllegalArgumentException("Stock cannot be negative");
        if (expirationDate == null)
            throw new IllegalArgumentException("Expiration date cannot be null");
    }
}
