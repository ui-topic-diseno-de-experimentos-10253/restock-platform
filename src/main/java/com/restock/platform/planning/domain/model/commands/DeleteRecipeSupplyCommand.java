package com.restock.platform.planning.domain.model.commands;

public record DeleteRecipeSupplyCommand(
        Long recipeId,
        Integer supplyId
) {
    public DeleteRecipeSupplyCommand {
        if (recipeId == null || recipeId <= 0)
            throw new IllegalArgumentException("Recipe ID must be a positive number.");
        if (supplyId <= 0)
            throw new IllegalArgumentException("Supply ID must be a positive number.");
    }
}