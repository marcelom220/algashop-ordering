package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null.");
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Money value cannot be negative.");
        }
        this.value = value.setScale(2, ROUNDING_MODE);
    }

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "Other money value cannot be null.");
        return new Money(this.value.add(other.value));
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity, "Quantity cannot be null.");
        if (quantity.value() < 1) {
            throw new IllegalArgumentException("Quantity for multiplication must be greater than or equal to 1.");
        }
        return new Money(this.value.multiply(new BigDecimal(quantity.value())));
    }

    public Money divide(Money other) {
        Objects.requireNonNull(other, "Other money value cannot be null.");
        if (other.value.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        BigDecimal result = this.value.divide(other.value(), 2, ROUNDING_MODE);
        return new Money(result);
    }

    @Override
    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return this.value.toPlainString();
    }
}
