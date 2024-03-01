package com.shopapp.product;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            value = "SELECT p.* " +
                    "FROM product p " +
                    "JOIN product_category pc ON p.product_id = pc.product_id " +
                    "JOIN category c ON pc.category_id = c.category_id " +
                    "WHERE c.category_id = :categoryId",
            nativeQuery = true
    )
    List<Product> findAllByCategoryId(@Param("categoryId") Long id);
    boolean existsProductByName(String name);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Product p WHERE p.productId = ?1")
    Integer deleteProductById(Long id);
}
