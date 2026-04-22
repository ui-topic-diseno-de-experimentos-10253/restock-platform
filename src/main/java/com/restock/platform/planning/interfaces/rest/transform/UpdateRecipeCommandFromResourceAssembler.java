package com.restock.platform.planning.interfaces.rest.transform;

import com.restock.platform.planning.domain.model.commands.UpdateRecipeCommand;
import com.restock.platform.planning.interfaces.rest.resources.UpdateRecipeResource;

public class UpdateRecipeCommandFromResourceAssembler {
    public static UpdateRecipeCommand toCommandFromResource(Long id, UpdateRecipeResource resource) {
        return new UpdateRecipeCommand(
                id,
                resource.name(),
                resource.description(),
                resource.imageUrl(),
                resource.price()
        );
    }
}