package com.example.product;

import com.example.category.Category;
import com.example.category.CategoryRepository;
import com.example.discount.Discount;
import com.example.discount.DiscountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DiscountRepository discountRepository;

    private Category category;
    private Discount discount;
    private Product testProduct;


    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Test")
                .description("Test category description")
                .build();
        categoryRepository.save(category);
        discount = Discount.builder()
                .name("Test")
                .description("Test discount description")
                .discountPercent(25)
                .build();
        discountRepository.save(discount);

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
    void findAllByCategoryIdReturnsProperProducts() {
        Product product2 = Product.builder()
                .name("Apple")
                .description("Apple to eat")
                .amount(115)
                .price(0.5)
                .discount(discount)
                .categories(List.of(category))
                .build();
        Product product3 = Product.builder()
                .name("Beer")
                .description("Beer to drink")
                .amount(250)
                .price(3.0)
                .discount(discount)
                .categories(List.of(category))
                .build();
        productRepository.save(testProduct);
        productRepository.save(product2);
        productRepository.save(product3);

        List<Product> products = productRepository.findAllByCategoryId(category.getCategoryId());
        assertEquals(products.size(), 3);
        assertEquals(products.get(0), testProduct);
        assertEquals(products.get(1), product2);
        assertEquals(products.get(2), product3);
    }

    @Test
    void findAllByCategoryIdReturnsEmptyList() {
        List<Product> products = productRepository.findAllByCategoryId(999L);
        assertTrue(products.isEmpty());
    }

    @Test
    void deleteProductByProductIdDeletesProperProduct() {
        productRepository.save(testProduct);

        List<Product> products = productRepository.findAll();
        assertEquals(products.size(), 1);

        Integer isDeleted = productRepository.deleteProductById(products.get(0).getProductId());
        assertEquals(isDeleted, 1);

        products = productRepository.findAll();
        assertEquals(products.size(), 0);
    }

    @Test
    void deleteProductByProductIdDoesNotDeleteAnythingIfProductDoesNotExist() {
        productRepository.save(testProduct);

        List<Product> products = productRepository.findAll();
        assertEquals(products.size(), 1);

        Integer isDeleted = productRepository.deleteProductById(999L);
        assertEquals(isDeleted, 0);

        products = productRepository.findAll();
        assertEquals(products.size(), 1);
    }
}