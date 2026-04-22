package com.restock.platform.planning.domain.model.commands;

public record AddRecipeSupplyCommand(
        Long recipeId,
        Integer supplyId,
        Double quantity
) {
    public AddRecipeSupplyCommand {
        if (recipeId == null || recipeId <= 0)
            throw new IllegalArgumentException("Recipe ID must be a positive number.");
        if (supplyId <= 0)
            throw new IllegalArgumentException("Supply ID must be a positive number.");
        if (quantity == null || quantity < 0)
            throw new IllegalArgumentException("Quantity must be non-negative");
    }
}