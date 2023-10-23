package com.example.shopapp.productphoto;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, UUID> {

    List<ProductPhoto> findAllPhotosByProductProductId(Long id);
    @Transactional
    Integer deletePhotoByPhotoName(UUID photoName);
}
