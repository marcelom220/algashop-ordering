package com.algaworks.algashop.ordering.domain.valueobject;

import static com.algaworks.algashop.ordering.domain.validator.FieldValidations.requiresValidEmail;

public record Email(String value) {

    public Email{
        requiresValidEmail(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
