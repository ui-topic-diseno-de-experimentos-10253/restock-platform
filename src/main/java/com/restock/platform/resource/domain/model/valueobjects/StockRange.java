package com.restock.platform.resource.domain.model.valueobjects;

public record StockRange(
        Double minStock,
        Double maxStock
) {

    public StockRange {

        if (minStock == null || maxStock == null) {
            throw new IllegalArgumentException("minStock and maxStock cannot be null");
        }

        if (minStock < 0) {
            throw new IllegalArgumentException("minStock cannot be negative");
        }

        if (maxStock < minStock) {
            throw new IllegalArgumentException("maxStock must be >= minStock");
        }
    }

    public boolean isInRange(double value) {
        return value >= minStock && value <= maxStock;
    }
}