package com.restock.platform.planning.interfaces.rest.resources;

import java.util.List;

public record RecipeResource(
        Long id,
        String name,
        String description,
        String imageUrl,
        Double price,
        int userId,
        List<RecipeSupplyResource> supplies
) {
}
