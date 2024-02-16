package com.example.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .name("Test")
                .description("Test Category description")
                .build();
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(categoryRepository).isNotNull();
    }

    @Test
    void findCategoryByNameReturnsProperCategory() {
        categoryRepository.save(testCategory);

        Optional<Category> categoryDB = categoryRepository.findCategoryByName(testCategory.getName());

        assertDoesNotThrow(categoryDB::get);
        assertEquals(categoryDB.get().getName(), testCategory.getName());
    }

    @Test
    void findCategoryByNameThrowsNoSuchElementException() {
        categoryRepository.save(testCategory);

        Optional<Category> categoryDB = categoryRepository.findCategoryByName("Bad name");

        assertThrows(NoSuchElementException.class, categoryDB::get);
    }

    @Test
    void deleteCategoryByCategoryIdDeletesProperCategory() {
        categoryRepository.save(testCategory);

        List<Category> categories = categoryRepository.findAll();
        assertEquals(categories.size(), 1);

        Integer isDeleted = categoryRepository.deleteCategoryById(categories.get(0).getCategoryId());
        assertEquals(isDeleted, 1);

        categories = categoryRepository.findAll();
        assertEquals(categories.size(), 0);
    }

    @Test
    void deleteCategoryByCategoryIdDoesNotDeleteAnythingIfCategoryDoesNotExist() {
        categoryRepository.save(testCategory);

        List<Category> categories = categoryRepository.findAll();
        assertEquals(categories.size(), 1);

        Integer isDeleted = categoryRepository.deleteCategoryById(999L);
        assertEquals(isDeleted, 0);

        categories = categoryRepository.findAll();
        assertEquals(categories.size(), 1);
    }
}