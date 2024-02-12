package com.example.shopapp.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            value = "SELECT od.* " +
                    "FROM order_details od " +
                    "JOIN order_product op ON od.order_id = op.order_id " +
                    "WHERE op.product_id = :productId",
            nativeQuery = true
    )
    List<Order> findAllByProductId(@Param("productId") Long id);
    @Query(value = "SELECT o FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2")
    List<Order> findAllByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate);
    @Query(value = "SELECT o FROM Order o JOIN o.user u WHERE u.userId = ?1")
    List<Order> findAllByUserId(Long id);
    @Query(value = "SELECT o FROM Order o WHERE o.isCompleted = ?1")
    List<Order> findAllByCompletionStatus(boolean isCompleted);
}
