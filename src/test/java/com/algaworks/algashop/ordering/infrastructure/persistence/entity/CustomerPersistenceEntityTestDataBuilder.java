package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerPersistenceEntityTestDataBuilder {

    private CustomerPersistenceEntityTestDataBuilder() {
    }

    public static CustomerPersistenceEntity.CustomerPersistenceEntityBuilder aCustomer() {
        return CustomerPersistenceEntity.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("john.doe@example.com")
                .phone("123456789")
                .document("12345678900")
                .promotionNotificationsAllowed(true)
                .archived(false)
                .registeredAt(OffsetDateTime.now())
                .loyaltyPoints(100)
                .address(AddressEmbeddable.builder()
                        .street("Main Street")
                        .number("123")
                        .neighborhood("Downtown")
                        .city("New York")
                        .state("NY")
                        .zipCode("10001")
                        .build());
    }
}
