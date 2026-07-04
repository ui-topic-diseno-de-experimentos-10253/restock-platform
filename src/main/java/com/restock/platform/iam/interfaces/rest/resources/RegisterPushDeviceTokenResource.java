package com.restock.platform.iam.interfaces.rest.resources;

public record RegisterPushDeviceTokenResource(
        Long userId,
        String platform,
        String pushToken
) {
    public RegisterPushDeviceTokenResource {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (platform == null || platform.isBlank()) {
            throw new IllegalArgumentException("Platform is required");
        }
        if (pushToken == null || pushToken.isBlank()) {
            throw new IllegalArgumentException("Push token is required");
        }
    }
}
