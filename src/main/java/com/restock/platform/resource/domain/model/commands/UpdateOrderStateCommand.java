package com.restock.platform.resource.domain.model.commands;

import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierSituation;
import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierState;

/**
 * Command to update the state and situation of an existing order.
 */
public record UpdateOrderStateCommand(
        Long orderId,
        OrderToSupplierState newState,
        OrderToSupplierSituation newSituation
) {}
