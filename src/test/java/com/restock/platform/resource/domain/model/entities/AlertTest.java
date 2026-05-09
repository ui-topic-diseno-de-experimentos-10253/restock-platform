package com.restock.platform.resource.domain.model.entities;

import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierSituation;
import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertTest {

    @Test
    void shouldCreateAlertSuccessfully() {
        Alert alert = new Alert("Order approved", 1L, OrderToSupplierSituation.APPROVED, 100L, 200L);
        
        assertNotNull(alert);
        assertEquals("Order approved", alert.getMessage());
        assertEquals(1L, alert.getOrderId());
        assertEquals(OrderToSupplierSituation.APPROVED, alert.getSituationAtAlert());
        assertEquals(OrderToSupplierState.ON_HOLD, alert.getStateAtAlert());
        assertEquals(100L, alert.getSupplierId());
        assertEquals(200L, alert.getAdminRestaurantId());
        assertNotNull(alert.getDate());
    }

    @Test
    void shouldUpdateStatusCorrectly() {
        Alert alert = new Alert("Pending order", 1L, OrderToSupplierSituation.PENDING, 100L, 200L);
        
        alert.updateStatus(OrderToSupplierState.ON_THE_WAY, OrderToSupplierSituation.APPROVED);
        
        assertEquals(OrderToSupplierState.ON_THE_WAY, alert.getStateAtAlert());
        assertEquals(OrderToSupplierSituation.APPROVED, alert.getSituationAtAlert());
    }

    @Test
    void shouldRequireUrgentAttentionWhenDeclined() {
        Alert alert = new Alert("Order declined", 1L, OrderToSupplierSituation.DECLINED, 100L, 200L);
        assertTrue(alert.requiresUrgentAttention());
    }

    @Test
    void shouldRequireUrgentAttentionWhenCancelled() {
        Alert alert = new Alert("Order cancelled", 1L, OrderToSupplierSituation.CANCELLED, 100L, 200L);
        assertTrue(alert.requiresUrgentAttention());
    }

    @Test
    void shouldNotRequireUrgentAttentionWhenApproved() {
        Alert alert = new Alert("Order approved", 1L, OrderToSupplierSituation.APPROVED, 100L, 200L);
        assertFalse(alert.requiresUrgentAttention());
    }

    @Test
    void shouldReturnSituationDescription() {
        Alert alert = new Alert("Order pending", 1L, OrderToSupplierSituation.PENDING, 100L, 200L);
        assertEquals("PENDING", alert.getSituationDescription());
    }
}
