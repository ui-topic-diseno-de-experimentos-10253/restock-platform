package com.restock.platform.iam.domain.model.aggregates;

import com.restock.platform.iam.domain.model.entities.Role;
import com.restock.platform.iam.domain.model.valueobjects.Roles;
import com.restock.platform.iam.domain.model.valueobjects.SubscriptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role(Roles.RestaurantAdministrator);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User("johndoe", "securePassword123", testRole);
        
        assertNotNull(user);
        assertEquals("johndoe", user.getUsername());
        assertEquals("securePassword123", user.getPassword());
        assertEquals(testRole, user.getRole());
        assertEquals(SubscriptionType.NO_SUBSCRIPTION.getValue(), user.getSubscription());
        assertNotNull(user.getProfile());
    }

    @Test
    void shouldGetRoleNameCorrectly() {
        User user = new User("johndoe", "password", testRole);
        
        assertEquals("RestaurantAdministrator", user.getRoleName());
    }

    @Test
    void shouldReturnNullWhenRoleIsNull() {
        User user = new User("johndoe", "password", null);
        
        assertNull(user.getRoleName());
    }

    @Test
    void shouldUpdateSubscriptionSuccessfully() {
        User user = new User();
        
        user.updateSubscription(SubscriptionType.MONTHLY_PLAN.getValue());
        
        assertEquals(SubscriptionType.MONTHLY_PLAN.getValue(), user.getSubscription());
        assertEquals(SubscriptionType.MONTHLY_PLAN, user.getSubscriptionType());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidSubscriptionValue() {
        User user = new User();
        
        assertThrows(IllegalArgumentException.class, () -> user.updateSubscription(99));
    }
}
