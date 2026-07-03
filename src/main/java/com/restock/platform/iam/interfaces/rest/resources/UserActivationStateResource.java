package com.restock.platform.iam.interfaces.rest.resources;

public record UserActivationStateResource(
        Long userId,
        boolean hasCreatedFirstOrder,
        long createdOrdersCount
) {
}
