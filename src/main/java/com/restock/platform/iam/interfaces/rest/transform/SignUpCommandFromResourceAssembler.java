package com.restock.platform.iam.interfaces.rest.transform;

import com.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.restock.platform.iam.domain.model.entities.Role;
import com.restock.platform.iam.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(resource.username(), resource.password(), resource.roleId());
    }
}