package com.restock.platform.resource.domain.model.commands;

public record DeleteCustomSupplyCommand(Long supplyId) {
    public DeleteCustomSupplyCommand {
        if (supplyId == null || supplyId <= 0) {
            throw new IllegalArgumentException("Supply ID must be a positive number");
        }
    }
}
