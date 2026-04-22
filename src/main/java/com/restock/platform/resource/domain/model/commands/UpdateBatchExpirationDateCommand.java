package com.restock.platform.resource.domain.model.commands;

import java.time.LocalDate;

public record UpdateBatchExpirationDateCommand(
        Long batchId,
        LocalDate newExpirationDate
) {
    public UpdateBatchExpirationDateCommand {
        if (batchId == null || batchId <= 0)
            throw new IllegalArgumentException("Batch ID must be a positive number");
        if (newExpirationDate == null)
            throw new IllegalArgumentException("New expiration date cannot be null");
    }
}
