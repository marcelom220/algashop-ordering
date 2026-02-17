package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value)  {
    public ShoppingCartId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartId() {

        this(UUID.randomUUID());
    }

    public static ShoppingCartId of(String value) {

        return new ShoppingCartId(UUID.fromString(value));
    }
}
