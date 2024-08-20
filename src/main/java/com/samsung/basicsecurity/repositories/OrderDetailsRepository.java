package com.samsung.basicsecurity.repositories;

import com.samsung.basicsecurity.repositories.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {
}
