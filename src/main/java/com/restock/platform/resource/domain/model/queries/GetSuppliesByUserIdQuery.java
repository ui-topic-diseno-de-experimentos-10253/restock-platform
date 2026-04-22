package com.restock.platform.resource.domain.model.queries;

public record GetSuppliesByUserIdQuery(Long userId) {
    public GetSuppliesByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
    }
}
