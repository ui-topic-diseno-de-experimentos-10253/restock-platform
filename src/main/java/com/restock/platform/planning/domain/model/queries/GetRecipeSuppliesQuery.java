package com.restock.platform.planning.domain.model.queries;

public record GetRecipeSuppliesQuery(Long recipeId) {
    public GetRecipeSuppliesQuery {
        if (recipeId == null || recipeId <= 0)
            throw new IllegalArgumentException("Recipe ID must be a positive number.");
    }
}