package com.restock.platform.resource.domain.model.queries;

public record GetBatchesByUserIdQuery(Long userId) {
    public GetBatchesByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
    }
}
