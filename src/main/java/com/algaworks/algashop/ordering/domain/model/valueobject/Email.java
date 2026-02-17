package com.algaworks.algashop.ordering.domain.model.valueobject;

import static com.algaworks.algashop.ordering.domain.model.validator.FieldValidations.requiresValidEmail;

public record Email(String value) {

    public Email{
        requiresValidEmail(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
