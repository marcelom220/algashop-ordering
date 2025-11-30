package com.algaworks.algashop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

public record Address(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
    @Builder(toBuilder = true)
    public Address {
        Objects.requireNonNull(street, "Street cannot be null.");
        Objects.requireNonNull(number, "Number cannot be null.");
        Objects.requireNonNull(neighborhood, "Neighborhood cannot be null.");
        Objects.requireNonNull(city, "City cannot be null.");
        Objects.requireNonNull(state, "State cannot be null.");
        Objects.requireNonNull(zipCode, "Zip code cannot be null.");
    }
}
