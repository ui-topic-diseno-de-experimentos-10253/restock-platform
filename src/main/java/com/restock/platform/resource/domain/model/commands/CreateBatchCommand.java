package com.restock.platform.resource.domain.model.commands;

import java.time.LocalDate;

public record CreateBatchCommand(
        Long userId,
        Long customSupplyId,
        double stock,
        LocalDate expirationDate
) {
    public CreateBatchCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("User ID must be a positive number");
        if (customSupplyId == null || customSupplyId <= 0)
            throw new IllegalArgumentException("Custom Supply ID must be a positive number");
        if (stock < 0)
            throw new IllegalArgumentException("Stock cannot be negative");
        if (expirationDate == null)
            throw new IllegalArgumentException("Expiration date cannot be null");
    }
}
