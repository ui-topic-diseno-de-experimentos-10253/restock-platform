package com.restock.platform.resource.domain.services;

import com.restock.platform.resource.domain.model.commands.SeedSuppliesCommand;

public interface SupplyCommandService {
    void handle(SeedSuppliesCommand command);
}
