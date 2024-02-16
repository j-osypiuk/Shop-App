package com.example.discount;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class DiscountRepositoryTest {

    @Autowired
    private DiscountRepository discountRepository;
    private Discount testDiscount;

    @BeforeEach
    void setUp() {
        testDiscount = Discount.builder()
                .name("Test")
                .description("Test discount description")
                .discountPercent(25)
                .build();
    }

    @AfterEach
    void tearDown() {
        discountRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(discountRepository).isNotNull();
    }

    @Test
    void deleteDiscountByDiscountIdDeletesProperDiscount() {
        discountRepository.save(testDiscount);

        List<Discount> discounts = discountRepository.findAll();
        assertEquals(discounts.size(), 1);

        Integer isDeleted = discountRepository.deleteDiscountById(discounts.get(0).getDiscountId());
        assertEquals(isDeleted, 1);

        discounts = discountRepository.findAll();
        assertEquals(discounts.size(), 0);
    }

    @Test
    void deleteDiscountByDiscountIdDoesNotDeleteAnythingIfDiscountDoesNotExist() {
        discountRepository.save(testDiscount);

        List<Discount> discounts = discountRepository.findAll();
        assertEquals(discounts.size(), 1);

        Integer isDeleted = discountRepository.deleteDiscountById(999L);
        assertEquals(isDeleted, 0);

        discounts = discountRepository.findAll();
        assertEquals(discounts.size(), 1);
    }
}