package com.samsung.basicsecurity.repositories;

import com.samsung.basicsecurity.repositories.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
