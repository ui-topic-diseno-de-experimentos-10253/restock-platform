package com.restock.platform.resource.interfaces.rest.resources;

import java.util.List;

public record AssistedCatalogUploadResource(
        Long supplierId,
        String assistedBy,
        String demoSessionCode,
        List<CreateCustomSupplyResource> items
) {
    public AssistedCatalogUploadResource {
        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("Supplier ID must be a positive number");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Catalog items are required");
        }
    }
}
