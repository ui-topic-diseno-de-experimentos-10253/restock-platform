package com.restock.platform.planning.interfaces.rest.transform;

import com.restock.platform.planning.domain.model.commands.AddRecipeSupplyCommand;
import com.restock.platform.planning.interfaces.rest.resources.AddRecipeSupplyResource;

public class AddRecipeSupplyCommandFromResourceAssembler {
    public static AddRecipeSupplyCommand toCommandFromResource(Long recipeId, AddRecipeSupplyResource resource) {
        return new AddRecipeSupplyCommand(
                recipeId,
                resource.supplyId(),
                resource.quantity()
        );
    }
}