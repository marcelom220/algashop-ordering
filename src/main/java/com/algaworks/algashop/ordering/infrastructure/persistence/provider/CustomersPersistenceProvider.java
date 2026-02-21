package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomersPersistenceProvider implements Customers {

    private final CustomerPersistenceEntityRepository repository;
    private final CustomerPersistenceEntityAssembler assembler;
    private final CustomerPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Customer> ofId(CustomerId id) {
        return repository.findById(id.value())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(CustomerId id) {
        return repository.existsById(id.value());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(Customer customer) {
        UUID customerId = customer.id().value();

        repository.findById(customerId)
                .ifPresentOrElse(
                        (persistenceEntity) -> update(customer, persistenceEntity),
                        () -> insert(customer)
                );
    }

    @Override
    public long count() {
        return repository.count();
    }

    private void update(Customer customer, CustomerPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, customer);
        entityManager.detach(persistenceEntity);
        repository.saveAndFlush(persistenceEntity);
        updateVersion(customer, persistenceEntity);
    }

    private void insert(Customer customer) {
        CustomerPersistenceEntity persistenceEntity = assembler.toPersistenceEntity(customer);
        repository.saveAndFlush(persistenceEntity);
        updateVersion(customer, persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Customer customer, CustomerPersistenceEntity persistenceEntity) {
        Field version = customer.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, customer, persistenceEntity.getVersion());
        version.setAccessible(false);
    }
}
