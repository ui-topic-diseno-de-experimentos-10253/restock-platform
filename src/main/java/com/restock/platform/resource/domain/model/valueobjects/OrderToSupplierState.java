package com.restock.platform.resource.domain.model.valueobjects;

/**
 * Enumeration representing the status of an order to supplier.
 * @summary
 * This enum defines the possible states of an order. It is used to track the status of the progress of an order.
 * The possible values are:
 * - ON_HOLD: The order is on hold and has not yet been processed.
 * - PREPARING: The order is being prepared by the supplier.
 * - ON_THE_WAY: The order is on its way to the admin restaurant.
 * - DELIVERED: The order has been delivered to the admin restaurant..
 * @since 1.0.0
 */
public enum OrderToSupplierState {
    ON_HOLD, //id = 1
    PREPARING, //id = 2
    ON_THE_WAY, //id = 3
    DELIVERED //id = 4
}
