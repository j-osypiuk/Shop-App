package com.example.shopapp.category;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c FROM Category c WHERE c.name = ?1")
    Optional<Category> findCategoryByName(String name);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Category c WHERE c.categoryId = ?1")
    Integer deleteCategoryById(Long id);
}
