package com.example.shopapp.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o FROM Order o JOIN o.products p WHERE p.productId = ?1")
    List<Order> findAllByProductId(Long id);
    @Query(value = "SELECT o FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2")
    List<Order> findAllByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate);
    @Query(value = "SELECT o FROM Order o JOIN o.user u WHERE u.userId = ?1")
    List<Order> findAllByUserId(Long id);
    @Query(value = "SELECT o FROM Order o WHERE o.isCompleted = ?1")
    List<Order> findAllByCompletionStatus(boolean isCompleted);
}
