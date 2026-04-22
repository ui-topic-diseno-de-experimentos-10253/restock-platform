package com.restock.platform.resource.domain.model.commands;

public record UpdateBatchStockCommand(
        Long batchId,
        double newStock
) {
    public UpdateBatchStockCommand {
        if (batchId == null || batchId <= 0)
            throw new IllegalArgumentException("Batch ID must be a positive number");
        if (newStock < 0)
            throw new IllegalArgumentException("New stock cannot be negative");
    }
}
