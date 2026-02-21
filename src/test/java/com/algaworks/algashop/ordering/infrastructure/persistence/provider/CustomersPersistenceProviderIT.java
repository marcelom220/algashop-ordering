package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.HibernateConfiguration;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class,
        HibernateConfiguration.class
})
class CustomersPersistenceProviderIT {

    private final CustomersPersistenceProvider persistenceProvider;
    private final CustomerPersistenceEntityRepository entityRepository;

    @Autowired
    public CustomersPersistenceProviderIT(CustomersPersistenceProvider persistenceProvider, CustomerPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        UUID customerId = customer.id().value();
        persistenceProvider.add(customer);

        CustomerPersistenceEntity persistenceEntity = entityRepository.findById(customerId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getFirstName()).isEqualTo(customer.fullName().firstName());
        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        customer = persistenceProvider.ofId(customer.id()).orElseThrow();
        customer.changeName(new com.algaworks.algashop.ordering.domain.model.valueobject.FullName("Jane", "Doe"));
        persistenceProvider.add(customer);

        persistenceEntity = entityRepository.findById(customerId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getFirstName()).isEqualTo("Jane");
        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldAddFindAndNotFailWhenNoTransaction() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        persistenceProvider.add(customer);

        Assertions.assertThatNoException().isThrownBy(
                () -> persistenceProvider.ofId(customer.id()).orElseThrow()
        );
    }
}
