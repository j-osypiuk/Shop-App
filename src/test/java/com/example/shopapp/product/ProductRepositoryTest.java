package com.example.shopapp.product;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.CategoryRepository;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.discount.DiscountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DiscountRepository discountRepository;
    private Product testProduct;


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

        testProduct = Product.builder()
                .name("Water")
                .description("Water to drink")
                .amount(100)
                .price(2.5)
                .discount(discount)
                .categories(List.of(category))
                .build();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(categoryRepository).isNotNull();
        assertThat(discountRepository).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    @Test
    void deleteProductByProductIdDeletesProperProduct() {
        productRepository.save(testProduct);

        List<Product> products = productRepository.findAll();
        assertEquals(products.size(), 1);

        Integer isDeleted = productRepository.deleteProductByProductId(products.get(0).getProductId());
        assertEquals(isDeleted, 1);

        products = productRepository.findAll();
        assertEquals(products.size(), 0);
    }

    @Test
    void deleteProductByProductIdDoesNotDeleteAnythingIfProductDoesNotExist() {
        productRepository.save(testProduct);

        List<Product> products = productRepository.findAll();
        assertEquals(products.size(), 1);

        Integer isDeleted = productRepository.deleteProductByProductId(999L);
        assertEquals(isDeleted, 0);

        products = productRepository.findAll();
        assertEquals(products.size(), 1);
    }
}