package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Objects.requireNonNull(value, "Quantity cannot be null.");
        if (value < 0) {
            throw new IllegalArgumentException("Quantity must be zero or positive.");
        }
    }

    public Quantity add(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null.");
        return new Quantity(this.value + other.value());
    }

    @Override
    public int compareTo(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null.");
        return this.value.compareTo(other.value());
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
