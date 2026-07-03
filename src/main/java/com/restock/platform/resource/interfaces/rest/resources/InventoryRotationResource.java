package com.restock.platform.resource.interfaces.rest.resources;

public record InventoryRotationResource(
        Long customSupplyId,
        Long supplyId,
        String supplyName,
        String rotationLevel,
        Integer consumedUnits,
        Double currentStock
) {
}
