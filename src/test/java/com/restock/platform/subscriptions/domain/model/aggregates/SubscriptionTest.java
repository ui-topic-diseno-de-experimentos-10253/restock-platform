package com.restock.platform.subscriptions.domain.model.aggregates;

import com.restock.platform.subscriptions.domain.model.valueobjects.SubscriptionPlan;
import com.restock.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    @Test
    void shouldCreateSubscriptionSuccessfully() {
        Subscription subscription = new Subscription(1L);

        assertNotNull(subscription);
        assertEquals(1L, subscription.getUserId());
        assertEquals(0, subscription.getPlan());
        assertEquals("none", subscription.getStatus());
        assertEquals(0, subscription.getDurationInMonths());
        assertNull(subscription.getStartDate());
        assertNull(subscription.getEndDate());
    }

    @Test
    void shouldUpdatePlanToMonthly() {
        Subscription subscription = new Subscription(1L);

        subscription.updatePlan(1);

        assertEquals(1, subscription.getPlan());
        assertEquals(SubscriptionPlan.MONTHLY_PLAN, subscription.getSubscriptionPlan());
        assertEquals("active", subscription.getStatus());
        assertEquals(SubscriptionStatus.ACTIVE, subscription.getSubscriptionStatus());
        assertEquals(1, subscription.getDurationInMonths());
        assertNotNull(subscription.getStartDate());
        assertNotNull(subscription.getEndDate());
        assertTrue(subscription.getEndDate().isAfter(subscription.getStartDate()));
    }

    @Test
    void shouldUpdatePlanToAnnual() {
        Subscription subscription = new Subscription(1L);

        subscription.updatePlan(2);

        assertEquals(2, subscription.getPlan());
        assertEquals("active", subscription.getStatus());
        assertEquals(12, subscription.getDurationInMonths());
    }

    @Test
    void shouldUpdatePlanToNone() {
        Subscription subscription = new Subscription(1L);
        subscription.updatePlan(1); // Set to monthly first
        
        subscription.updatePlan(0); // Update back to none

        assertEquals(0, subscription.getPlan());
        assertEquals("none", subscription.getStatus());
        assertEquals(0, subscription.getDurationInMonths());
        assertNull(subscription.getStartDate());
        assertNull(subscription.getEndDate());
    }

    @Test
    void shouldUpdateStatusToExpiredWhenEndDateIsPast() {
        Subscription subscription = new Subscription(1L);
        subscription.updatePlan(1); // Set monthly
        
        // Manually manipulate end date to a past date
        subscription.setEndDate(LocalDateTime.now().minusDays(1));
        
        subscription.updateStatus();
        
        assertEquals("expired", subscription.getStatus());
        assertEquals(SubscriptionStatus.EXPIRED, subscription.getSubscriptionStatus());
    }

    @Test
    void shouldKeepStatusActiveWhenEndDateIsInFuture() {
        Subscription subscription = new Subscription(1L);
        subscription.updatePlan(1); // Set monthly
        
        subscription.updateStatus();
        
        assertEquals("active", subscription.getStatus());
    }
}
