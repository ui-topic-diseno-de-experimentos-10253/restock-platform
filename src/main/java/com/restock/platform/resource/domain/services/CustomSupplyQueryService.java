package com.restock.platform.resource.domain.services;

import com.restock.platform.resource.domain.model.aggregates.CustomSupply;
import com.restock.platform.resource.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface CustomSupplyQueryService {
    List<CustomSupply> handle(GetAllCustomSuppliesQuery query);
    Optional<CustomSupply> handle(GetCustomSupplyByIdQuery query);
    List<CustomSupply> handle(GetCustomSuppliesByUserIdQuery query);
}
