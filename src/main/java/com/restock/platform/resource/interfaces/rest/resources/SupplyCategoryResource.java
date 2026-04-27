package com.restock.platform.resource.interfaces.rest.resources;

import java.io.Serializable;

public record SupplyCategoryResource(
        String category
) implements Serializable {}
