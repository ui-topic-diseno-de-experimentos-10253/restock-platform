package com.restock.platform.resource.domain.model.aggregates;

import com.restock.platform.resource.domain.model.commands.CreateOrderCommand;
import com.restock.platform.resource.domain.model.valueobjects.OrderBatchItem;
import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierSituation;
import com.restock.platform.resource.domain.model.valueobjects.OrderToSupplierState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderTest {

    private CreateOrderCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = new CreateOrderCommand(
                1L,
                2L,
                new ArrayList<>(),
                "Test order",
                LocalDate.now().plusDays(2),
                LocalTime.NOON
        );
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        Order order = new Order(validCommand);

        assertNotNull(order);
        assertEquals(1L, order.getAdminRestaurantId());
        assertEquals(2L, order.getSupplierId());
        assertEquals("Test order", order.getDescription());
        assertEquals(OrderToSupplierState.ON_HOLD, order.getState());
        assertEquals(OrderToSupplierSituation.PENDING, order.getSituation());
        assertFalse(order.isPartiallyAccepted());
        assertEquals(0, order.getRequestedProductsCount());
        assertEquals(BigDecimal.ZERO, order.getTotalPrice());
    }

    @Test
    void shouldUpdateStateAndSituation() {
        Order order = new Order(validCommand);

        order.update(OrderToSupplierState.ON_THE_WAY, OrderToSupplierSituation.APPROVED);

        assertEquals(OrderToSupplierState.ON_THE_WAY, order.getState());
        assertEquals(OrderToSupplierSituation.APPROVED, order.getSituation());
    }

    @Test
    void shouldThrowExceptionWhenTotalPriceIsNegative() {
        Order order = new Order(validCommand);

        assertThrows(IllegalArgumentException.class, () -> order.setTotalPrice(new BigDecimal("-10.0")));
    }

    @Test
    void shouldThrowExceptionWhenRequestedProductsCountIsNegative() {
        Order order = new Order(validCommand);

        assertThrows(IllegalArgumentException.class, () -> order.setRequestedProductsCount(-5));
    }

    @Test
    void shouldRecalculateTotalsCorrectly() {
        Order order = new Order(validCommand);
        
        OrderBatchItem mockItem1 = Mockito.mock(OrderBatchItem.class);
        when(mockItem1.getQuantity()).thenReturn(5.0);
        when(mockItem1.getTotalPrice()).thenReturn(new BigDecimal("50.0"));

        OrderBatchItem mockItem2 = Mockito.mock(OrderBatchItem.class);
        when(mockItem2.getQuantity()).thenReturn(3.0);
        when(mockItem2.getTotalPrice()).thenReturn(new BigDecimal("30.0"));

        order.addBatchItem(mockItem1);
        order.addBatchItem(mockItem2);

        order.recalculateTotals();

        assertEquals(8, order.getRequestedProductsCount());
        assertEquals(new BigDecimal("80.0"), order.getTotalPrice());
    }
}
