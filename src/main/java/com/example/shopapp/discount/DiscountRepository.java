package com.example.shopapp.discount;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Transactional
    Integer deleteDiscountByDiscountId(Long id);
}
