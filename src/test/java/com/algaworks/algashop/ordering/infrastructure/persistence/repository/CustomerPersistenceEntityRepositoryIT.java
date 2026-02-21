package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.config.HibernateConfiguration;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SpringDataAuditingConfig.class, HibernateConfiguration.class})
class CustomerPersistenceEntityRepositoryIT {

    private final CustomerPersistenceEntityRepository repository;

    @Autowired
    public CustomerPersistenceEntityRepositoryIT(CustomerPersistenceEntityRepository repository) {
        this.repository = repository;
    }

    @Test
    void shouldPersist() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();

        repository.saveAndFlush(entity);
        Assertions.assertThat(repository.existsById(entity.getId())).isTrue();

        CustomerPersistenceEntity savedEntity = repository.findById(entity.getId()).orElseThrow();

        Assertions.assertThat(savedEntity.getFirstName()).isEqualTo(entity.getFirstName());
        Assertions.assertThat(savedEntity.getAddress()).isNotNull();
    }

    @Test
    void shouldUpdateAndIncrementVersion() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();
        entity = repository.saveAndFlush(entity);
        Long initialVersion = entity.getVersion();

        // To simulate an update on a detached entity or a new transaction, we fetch it again
        // Note: In a real scenario with EntityManager we would detach/clear.
        // Here we rely on modifying the returned instance and saving it.
        entity.setFirstName("Updated Name");
        CustomerPersistenceEntity updatedEntity = repository.saveAndFlush(entity);

        Assertions.assertThat(updatedEntity.getVersion()).isGreaterThan(initialVersion);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingOldVersion() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();
        entity = repository.saveAndFlush(entity);

        // Simulate concurrent update:
        // 1. Create a copy of the entity representing another transaction/thread holding the same version
        CustomerPersistenceEntity concurrentEntity = CustomerPersistenceEntity.builder()
                .id(entity.getId())
                .firstName("Concurrent Update")
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .document(entity.getDocument())
                .address(entity.getAddress())
                .version(entity.getVersion()) // Same version as 'entity'
                .build();

        // 2. Update the entity in the database (increments version)
        entity.setFirstName("First Update");
        repository.saveAndFlush(entity);

        // 3. Try to save the 'concurrentEntity' which now has an old version
        Assertions.assertThatThrownBy(() -> repository.saveAndFlush(concurrentEntity))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void shouldFindById() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();
        repository.saveAndFlush(entity);

        CustomerPersistenceEntity foundEntity = repository.findById(entity.getId()).orElseThrow();

        Assertions.assertThat(foundEntity.getId()).isEqualTo(entity.getId());
        Assertions.assertThat(foundEntity.getEmail()).isEqualTo(entity.getEmail());
    }

    @Test
    void shouldCount() {
        long initialCount = repository.count();
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();
        repository.saveAndFlush(entity);

        long count = repository.count();
        Assertions.assertThat(count).isEqualTo(initialCount + 1);
    }

    @Test
    void shouldSetAuditingValues() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.aCustomer().build();
        entity = repository.saveAndFlush(entity);

        Assertions.assertThat(entity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }
}
