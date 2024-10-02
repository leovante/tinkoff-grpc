package ru.tinkoff.adapter.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.adapter.db.entity.OrderDomainEntity;

public interface OrderRepository extends JpaRepository<OrderDomainEntity, Long> {
}
