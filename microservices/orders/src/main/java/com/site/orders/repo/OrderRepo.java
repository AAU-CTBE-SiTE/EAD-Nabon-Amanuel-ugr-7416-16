package com.site.orders.repo;

import com.site.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    // SELECT * FROM orders WHERE customer_id = ?
    List<Order> findByCustomerId(Long customerId);
}
