package com.restock.platform.resource.interfaces.rest.resources;

public record DeleteSupplyResource(Long supplyId) {
    public DeleteSupplyResource {
        if (supplyId == null || supplyId <= 0)
            throw new IllegalArgumentException("Supply ID must be a positive number.");
    }
}
