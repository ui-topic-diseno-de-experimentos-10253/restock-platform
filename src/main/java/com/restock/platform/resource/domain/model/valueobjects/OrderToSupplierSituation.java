package com.restock.platform.resource.domain.model.valueobjects;

/// Enumeration representing the situation of an order to supplier.
/// @summary
/// This enum defines the possible situations of an order. It is used to track the situation accept of an order.
/// The possible values are:
/// - PENDING: The order is pending and has not yet been approved or declined.
/// - APPROVED: The order has been approved by the supplier.
/// - DECLINED: The order has been declined by the supplier.
/// - CANCELLED: The order was accepted but has been cancelled by the supplier.
/// @since 1.0.0
public enum OrderToSupplierSituation {
    PENDING, //id = 1
    APPROVED, //id = 2
    DECLINED, //id = 3
    CANCELLED, //id = 4
}
