package com.restock.platform.resource.interfaces.rest.transform;

import com.restock.platform.resource.interfaces.rest.resources.SupplyCategoryResource;

public class SupplyCategoryResourceFromEntityAssembler {

    public static SupplyCategoryResource toResourceFromEntity(String category) {
        return new SupplyCategoryResource(category);
    }
}
