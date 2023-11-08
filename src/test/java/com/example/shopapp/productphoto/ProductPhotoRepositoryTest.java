package com.example.shopapp.productphoto;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.CategoryRepository;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.discount.DiscountRepository;
import com.example.shopapp.product.Product;
import com.example.shopapp.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ProductPhotoRepositoryTest {

    @Autowired
    ProductPhotoRepository productPhotoRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    DiscountRepository discountRepository;
    private List<ProductPhoto> testProductPhotos = new ArrayList<>();
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.save(Category.builder()
                .name("Test")
                .description("Test category description")
                .build());
        Discount discount = discountRepository.save(Discount.builder()
                .name("Test")
                .description("Test discount description")
                .discountPercent(25)
                .build());

        product1 = productRepository.save(Product.builder()
                .name("Water")
                .description("Water to drink")
                .amount(100)
                .price(2.5)
                .discount(discount)
                .categories(List.of(category))
                .build());

        product2 = productRepository.save(Product.builder()
                .name("Food")
                .description("Food to eat")
                .amount(150)
                .price(2.5)
                .discount(discount)
                .categories(List.of(category))
                .build());

        testProductPhotos.add(ProductPhoto.builder()
                .photoName(UUID.randomUUID())
                .number(0)
                .product(product1)
                .build());

        testProductPhotos.add(ProductPhoto.builder()
                .photoName(UUID.randomUUID())
                .number(1)
                .product(product1)
                .build());

        testProductPhotos.add(ProductPhoto.builder()
                .photoName(UUID.randomUUID())
                .number(0)
                .product(product2)
                .build());
    }

    @AfterEach
    void tearDown() {
        productPhotoRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(discountRepository).isNotNull();
        assertThat(categoryRepository).isNotNull();
        assertThat(productRepository).isNotNull();
        assertThat(productPhotoRepository).isNotNull();
    }

    @Test
    void findAllPhotosByProductProductIdFindsAllProperProductPhotos() {
        testProductPhotos.forEach(productPhoto -> productPhotoRepository.save(productPhoto));

        List<ProductPhoto> photos = productPhotoRepository.findAll();
        assertEquals(photos.size(), 3);

        List<ProductPhoto> firstProductPhotos = productPhotoRepository.findAllPhotosByProductProductId(product1.getProductId());
        assertEquals(firstProductPhotos.size(), 2);
        firstProductPhotos.forEach(productPhoto ->
                assertEquals(productPhoto.getProduct().getProductId(), product1.getProductId()));

        List<ProductPhoto> secondProductPhotos = productPhotoRepository.findAllPhotosByProductProductId(product2.getProductId());
        assertEquals(secondProductPhotos.size(), 1);
        secondProductPhotos.forEach(productPhoto ->
                assertEquals(productPhoto.getProduct().getProductId(), product2.getProductId()));

        List<ProductPhoto> notFoundProductPhotos = productPhotoRepository.findAllPhotosByProductProductId(999L);
        assertEquals(notFoundProductPhotos.size(), 0);
    }

    @Test
    void deletePhotoByPhotoNameDeletesProperProductPhoto() {
        productPhotoRepository.save(testProductPhotos.get(0));

        List<ProductPhoto> photos = productPhotoRepository.findAll();
        assertEquals(photos.size(), 1);

       Integer isDeleted = productPhotoRepository.deletePhotoByPhotoName(testProductPhotos.get(0).getPhotoName());
       assertEquals(isDeleted, 1);

       photos = productPhotoRepository.findAll();
       assertEquals(photos.size(), 0);
    }

    @Test
    void deletePhotoByPhotoNameDoesNotDeleteAnythingIfProductPhotoDoesNotExist() {
        productPhotoRepository.save(testProductPhotos.get(0));

        List<ProductPhoto> photos = productPhotoRepository.findAll();
        assertEquals(photos.size(), 1);

        Integer isDeleted = productPhotoRepository.deletePhotoByPhotoName(UUID.randomUUID());
        assertEquals(isDeleted, 0);

        photos = productPhotoRepository.findAll();
        assertEquals(photos.size(), 1);
    }
}