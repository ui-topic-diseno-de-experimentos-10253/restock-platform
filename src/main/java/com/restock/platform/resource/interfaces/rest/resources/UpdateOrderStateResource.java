package com.restock.platform.resource.interfaces.rest.resources;

import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierSituation;
import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierState;

public record UpdateOrderStateResource(
        Long orderId,
        OrderToSupplierState newState,
        OrderToSupplierSituation newSituation
) {
}
