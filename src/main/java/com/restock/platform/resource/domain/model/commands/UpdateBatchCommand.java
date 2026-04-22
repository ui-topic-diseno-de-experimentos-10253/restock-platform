package com.restock.platform.resource.domain.model.commands;

import java.time.LocalDate;

public record UpdateBatchCommand(
        Long batchId,
        Long userId,
        Long supplyId,
        Double stock,
        LocalDate expirationDate
) {
    public UpdateBatchCommand {
        if (batchId == null || batchId <= 0)
            throw new IllegalArgumentException("Batch ID must be a positive number.");

        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("User ID must be a positive number.");

        if (supplyId == null || supplyId <= 0)
            throw new IllegalArgumentException("Supply ID must be a positive number.");

        if (stock == null || stock < 0)
            throw new IllegalArgumentException("Stock must not be null or negative.");

        if (expirationDate == null)
            throw new IllegalArgumentException("Expiration date must not be null.");
    }
}
