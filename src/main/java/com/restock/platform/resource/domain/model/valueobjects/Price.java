package com.restock.platform.resource.domain.model.valueobjects;
import java.util.Currency;

public record Price(
        double amount,
        Currency currency  // Ej: Currency.getInstance("USD")
) {

    public Price {
        if (amount < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public Price applyDiscount(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }
        double discountedAmount = amount * (1 - discountPercentage / 100);
        return new Price(discountedAmount, currency);
    }
}