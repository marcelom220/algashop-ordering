package com.algaworks.algashop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record ShippingInfo(
        FullName fullName,
        Document document,
        Phone phone,
        Address address
) {
    public ShippingInfo {
        Objects.requireNonNull(fullName, "Full name cannot be null.");
        Objects.requireNonNull(document, "Document cannot be null.");
        Objects.requireNonNull(phone, "Phone cannot be null.");
        Objects.requireNonNull(address, "Address cannot be null.");
    }
}
