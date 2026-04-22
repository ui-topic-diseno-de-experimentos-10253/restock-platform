package com.restock.platform.resource.domain.services;

import com.restock.platform.resource.domain.model.aggregates.Batch;
import com.restock.platform.resource.domain.model.commands.*;

import java.util.Optional;

public interface BatchCommandService {
    Long handle(CreateBatchCommand command);
    Optional<Batch> handle(UpdateBatchCommand command);
    void handle(DeleteBatchCommand command);
}
