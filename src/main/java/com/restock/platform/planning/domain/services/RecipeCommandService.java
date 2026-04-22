package com.restock.platform.planning.domain.services;

import com.restock.platform.planning.domain.model.aggregates.Recipe;
import com.restock.platform.planning.domain.model.commands.*;

import java.util.Optional;

public interface RecipeCommandService {
    Long handle(CreateRecipeCommand command);

    Optional<Recipe> handle(UpdateRecipeCommand command);

    void handle(DeleteRecipeCommand command);

    Optional<Recipe> handle(AddRecipeSupplyCommand command);

    Optional<Recipe> handle(UpdateRecipeSupplyCommand command);

    Optional<Recipe> handle(DeleteRecipeSupplyCommand command);}
