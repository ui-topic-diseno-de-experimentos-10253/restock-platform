package com.restock.platform.resource.interfaces.rest.resources;

import java.time.LocalDate;

public record UpdateBatchResource(
        Long userId,
        Long customSupplyId,
        Double stock,
        LocalDate expirationDate
) {}
