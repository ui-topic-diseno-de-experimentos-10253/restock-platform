package com.restock.platform.iam.domain.model.entities;

import com.restock.platform.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleTest {

    @Test
    void shouldCreateRoleSuccessfully() {
        Role role = new Role(Roles.RestaurantAdministrator);
        
        assertNotNull(role);
        assertEquals(Roles.RestaurantAdministrator, role.getName());
    }

    @Test
    void shouldReturnStringNameCorrectly() {
        Role role = new Role(Roles.RestaurantSupplier);
        
        assertEquals("RestaurantSupplier", role.getStringName());
    }

    @Test
    void shouldConvertNameToRole() {
        Role role = Role.toRoleFromName("RestaurantAdministrator");
        
        assertNotNull(role);
        assertEquals(Roles.RestaurantAdministrator, role.getName());
    }
}
