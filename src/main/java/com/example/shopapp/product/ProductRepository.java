package com.example.shopapp.product;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Product p WHERE p.productId = ?1")
    Integer deleteProductById(Long id);
    boolean existsProductByName(String name);
}
