package com.restock.platform.planning.domain.model.commands;

public record DeleteRecipeCommand(Long recipeId) {
    public DeleteRecipeCommand {
        if (recipeId == null || recipeId <= 0)
            throw new IllegalArgumentException("Recipe ID must be a positive number.");
    }
}