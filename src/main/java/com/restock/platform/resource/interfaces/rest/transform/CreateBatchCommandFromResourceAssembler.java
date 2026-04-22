package com.restock.platform.resource.interfaces.rest.transform;

import com.restock.platform.resource.domain.model.commands.CreateBatchCommand;
import com.restock.platform.resource.interfaces.rest.resources.CreateBatchResource;

/**
 * Assembler to convert CreateBatchResource to CreateBatchCommand.
 */
public class CreateBatchCommandFromResourceAssembler {
    public static CreateBatchCommand toCommandFromResource(CreateBatchResource resource) {
        return new CreateBatchCommand(
                resource.userId(),
                resource.customSupplyId(),
                resource.stock(),
                resource.expirationDate()
        );
    }
}
