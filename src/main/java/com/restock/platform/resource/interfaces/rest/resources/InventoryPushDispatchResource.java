package com.restock.platform.resource.interfaces.rest.resources;

public record InventoryPushDispatchResource(
        Long userId,
        int candidates,
        int activeTokens,
        int sent,
        int failed
) {
}
