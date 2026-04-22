package com.restock.platform.resource.domain.services;

import com.restock.platform.resource.domain.model.entities.Supply;
import com.restock.platform.resource.domain.model.queries.GetAllSuppliesQuery;
import com.restock.platform.resource.domain.model.queries.GetAllSupplyCategoriesQuery;
import com.restock.platform.resource.domain.model.queries.GetSupplyByIdQuery;

import java.util.List;
import java.util.Optional;

public interface SupplyQueryService {
    List<Supply> handle(GetAllSuppliesQuery query);
    Optional<Supply> handle(GetSupplyByIdQuery query);
    List<String> handle(GetAllSupplyCategoriesQuery query);
}
