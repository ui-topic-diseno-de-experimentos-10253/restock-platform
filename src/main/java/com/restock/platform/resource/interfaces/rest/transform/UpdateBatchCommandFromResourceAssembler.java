package com.restock.platform.resource.interfaces.rest.transform;

import com.restock.platform.resource.domain.model.commands.UpdateBatchCommand;
import com.restock.platform.resource.interfaces.rest.resources.UpdateBatchResource;

public class UpdateBatchCommandFromResourceAssembler {
    public static UpdateBatchCommand toCommandFromResource(Long id, UpdateBatchResource resource) {
        return new UpdateBatchCommand(
                id,
                resource.userId(),
                resource.customSupplyId(),
                resource.stock(),
                resource.expirationDate()
        );
    }
}
