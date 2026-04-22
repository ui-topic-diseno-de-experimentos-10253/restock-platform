package com.restock.platform.planning.domain.model.commands;

public record CreateRecipeCommand(
        String name,
        String description,
        String imageUrl,
        Double price,
        Integer userId
) {
}
