package com.shopapp.discount;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Discount d WHERE d.discountId = ?1")
    Integer deleteDiscountById(Long id);
    boolean existsDiscountByName(String name);
}
