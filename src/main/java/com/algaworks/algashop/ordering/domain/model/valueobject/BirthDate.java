package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.validator.FieldValidations.requiresDateInPast;

public record BirthDate(LocalDate value) {

    public BirthDate{
        Objects.requireNonNull(value);
        requiresDateInPast(value);

    }

    public Integer age() {
        return (int) Period.between(value, LocalDate.now()).getDays();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
