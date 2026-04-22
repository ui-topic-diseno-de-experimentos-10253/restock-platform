package com.restock.platform.planning.interfaces.rest.transform;

import com.restock.platform.planning.domain.model.commands.CreateRecipeCommand;
import com.restock.platform.planning.interfaces.rest.resources.CreateRecipeResource;

/**
 * Assembler to transform CreateRecipeResource into CreateRecipeCommand.
 */
public class CreateRecipeCommandFromResourceAssembler {
    /**
     *Converts a CreateRecipeResource to a CreateRecipeCommand.
     * @param resource The{@link CreateRecipeResource} instance containing the recipe details.
     * @return The {@link CreateRecipeCommand} instance created from the resource.
     */
    public static CreateRecipeCommand toCommandFromResource(CreateRecipeResource resource) {
        return new CreateRecipeCommand(
                resource.name(),
                resource.description(),
                resource.imageUrl(),
                resource.price(),
                resource.userId()
        );
    }
}
