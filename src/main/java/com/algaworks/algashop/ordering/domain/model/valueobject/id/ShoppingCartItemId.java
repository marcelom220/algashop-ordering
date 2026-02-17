package com.algaworks.algashop.ordering.domain.model.valueobject.id;


import java.util.Objects;
import java.util.UUID;

public record ShoppingCartItemId(UUID value)  {
    public ShoppingCartItemId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartItemId() {

        this(UUID.randomUUID());
    }

    public static ShoppingCartItemId of(String value) {

        return new ShoppingCartItemId(UUID.fromString(value));
    }
}
