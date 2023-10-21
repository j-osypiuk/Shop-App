package com.example.shopapp.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByProductsProductId(Long id);
    List<Order> findAllByOrderDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
    List<Order> findAllByCustomerCustomerId(Long id);
    List<Order> findAllByIsCompleted(boolean isCompleted);
}
