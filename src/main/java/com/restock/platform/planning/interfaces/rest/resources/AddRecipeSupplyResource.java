package com.restock.platform.planning.interfaces.rest.resources;

public record AddRecipeSupplyResource(
        Integer supplyId,
        Double quantity
) {
    public AddRecipeSupplyResource {
        if (supplyId == null || supplyId <= 0)
            throw new IllegalArgumentException("Supply ID must be a positive number");
        if (quantity == null || quantity < 0)
            throw new IllegalArgumentException("Quantity must be non-negative");
    }
}