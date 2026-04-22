package com.restock.platform.resource.domain.model.queries;

public record GetSupplyByIdQuery(Long supplyId) {
    public GetSupplyByIdQuery {
        if (supplyId == null || supplyId <= 0) {
            throw new IllegalArgumentException("Supply ID must be a positive number");
        }
    }
}
