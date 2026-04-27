package com.restock.platform.resource.domain.model.queries;

public record GetBatchesBySupplyIdQuery(Long supplyId) {
    public GetBatchesBySupplyIdQuery {
        if (supplyId == null || supplyId <= 0) {
            throw new IllegalArgumentException("Supply ID must be a positive number");
        }
    }
}
