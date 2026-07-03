package com.restock.platform.resource.interfaces.rest.resources;

import java.util.List;

public record AssistedCatalogUploadResponseResource(
        Long supplierId,
        String assistedBy,
        String demoSessionCode,
        int uploadedItems,
        List<Long> customSupplyIds
) {
}
