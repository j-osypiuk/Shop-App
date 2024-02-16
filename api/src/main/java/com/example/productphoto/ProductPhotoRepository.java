package com.example.productphoto;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, UUID> {

    @Query(value = "SELECT pp FROM ProductPhoto pp JOIN pp.product p WHERE p.productId = ?1")
    List<ProductPhoto> findAllByProductId(Long id);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ProductPhoto p WHERE p.photoName = ?1")
    Integer deletePhotoByPhotoName(UUID photoName);
}
