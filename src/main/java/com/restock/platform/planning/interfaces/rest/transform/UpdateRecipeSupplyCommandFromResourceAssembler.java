package com.restock.platform.planning.interfaces.rest.transform;

import com.restock.platform.planning.domain.model.commands.UpdateRecipeSupplyCommand;
import com.restock.platform.planning.interfaces.rest.resources.AddRecipeSupplyResource;

public class UpdateRecipeSupplyCommandFromResourceAssembler {
    public static UpdateRecipeSupplyCommand toCommandFromResource(Long recipeId, Integer supplyId, AddRecipeSupplyResource resource) {
        return new UpdateRecipeSupplyCommand(
                recipeId,
                supplyId,
                resource.quantity()
        );
    }
}