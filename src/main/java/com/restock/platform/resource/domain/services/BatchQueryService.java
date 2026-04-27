package com.restock.platform.resource.domain.services;

import com.restock.platform.resource.domain.model.aggregates.Batch;
import com.restock.platform.resource.domain.model.queries.GetAllBatchesQuery;
import com.restock.platform.resource.domain.model.queries.GetBatchByIdQuery;
import com.restock.platform.resource.domain.model.queries.GetBatchesBySupplyIdQuery;
import com.restock.platform.resource.domain.model.queries.GetBatchesByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface BatchQueryService {
    List<Batch> handle(GetAllBatchesQuery query);
    Optional<Batch> handle(GetBatchByIdQuery query);
    List<Batch> handle(GetBatchesBySupplyIdQuery query);
    List<Batch> handle(GetBatchesByUserIdQuery query);
}
