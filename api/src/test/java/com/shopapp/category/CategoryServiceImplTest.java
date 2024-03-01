package com.shopapp.category;

import com.shopapp.exception.DuplicateUniqueValueException;
import com.shopapp.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void saveCategorySavesCategory() throws DuplicateUniqueValueException {
        // given
        Category category = Category.builder().build();
        given(categoryRepository.existsCategoryByName(category.getName())).willReturn(false);
        given(categoryRepository.save(category)).willReturn(category);
        // when
        Category savedCategory = categoryService.saveCategory(category);
        // then
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        ArgumentCaptor<String> categoryNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(categoryRepository).existsCategoryByName(categoryNameCaptor.capture());
        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryNameCaptor.getValue()).isEqualTo(category.getName());
        assertThat(categoryCaptor.getValue()).isEqualTo(savedCategory);
        assertEquals(savedCategory, category);
    }

    @Test
    void saveCategoryThrowsExceptionIfOtherExistingCategoryHasTheSameName() {
        // given
        Category category = Category.builder().name("food").build();
        given(categoryRepository.existsCategoryByName(category.getName())).willReturn(true);
        // when
        // then
        ArgumentCaptor<String> categoryNameCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> categoryService.saveCategory(category))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("Category with name = " + category.getName() + " already exists");
        verify(categoryRepository).existsCategoryByName(categoryNameCaptor.capture());
        verify(categoryRepository, never()).save(category);
        assertThat(categoryNameCaptor.getValue()).isEqualTo(category.getName());
    }

    @Test
    void getCategoryByIdReturnsCategory() throws ObjectNotFoundException {
        // given
        Long categoryId = 1L;
        Category category = Category.builder().categoryId(categoryId).build();
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
        // when
        Category foundCategory = categoryService.getCategoryById(categoryId);
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
        assertEquals(foundCategory, category);
    }

    @Test
    void getCategoryByIdThrowsExceptionIfCategoryDoesNotExists() {
        // given
        Long categoryId = 1L;
        given(categoryRepository.findById(categoryId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> categoryService.getCategoryById(categoryId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Category with id = " + categoryId + " not found");
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
    }

    @Test
    void getCategoryByNameReturnsCategory() throws ObjectNotFoundException {
        // given
        String categoryName = "Food";
        Category category = Category.builder().name(categoryName).build();
        given(categoryRepository.findCategoryByName(categoryName)).willReturn(Optional.of(category));
        // when
        Category foundCategory = categoryService.getCategoryByName(categoryName);
        // then
        ArgumentCaptor<String> categoryNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(categoryRepository).findCategoryByName(categoryNameCaptor.capture());
        assertEquals(foundCategory.getName(), categoryNameCaptor.getValue());
        assertThat(categoryNameCaptor.getValue()).isEqualTo(categoryName);
        assertEquals(foundCategory, category);
    }

    @Test
    void getCategoryByNameThrowsExceptionIfCategoryWithGivenNameDoesNotExists() {
        // given
        String categoryName = "Food";
        given(categoryRepository.findCategoryByName(categoryName)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<String> categoryNameCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> categoryService.getCategoryByName(categoryName))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Category with name = " + categoryName + " not found");
        verify(categoryRepository).findCategoryByName(categoryNameCaptor.capture());
        assertThat(categoryNameCaptor.getValue()).isEqualTo(categoryName);
    }

    @Test
    void getAllCategoriesReturnsListOfCategories() throws ObjectNotFoundException {
        // given
        Category category1 = Category.builder().name("drinks").description("Drinks category description").build();
        Category category2 = Category.builder().name("food").description("Food category description").build();
        given(categoryRepository.findAll()).willReturn(Arrays.asList(category1, category2));
        // when
        List<Category> categories = categoryService.getAllCategories();
        // then
        verify(categoryRepository).findAll();
        assertEquals(categories.size(), 2);
        assertEquals(categories.get(0), category1);
        assertEquals(categories.get(1), category2);
    }

    @Test
    void getAllCategoriesThrowsExceptionIfNoCategoriesExists() {
        // given
        given(categoryRepository.findAll()).willReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(() -> categoryService.getAllCategories())
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No categories found");
        verify(categoryRepository).findAll();
    }

    @Test
    void updateCategoryByIdUpdatesCategory() throws ObjectNotFoundException, DuplicateUniqueValueException {
        // given
        Long categoryId = 1L;
        Category foundCategory = Category.builder().name("drinks").description("Drinks category description").build();
        Category category = Category.builder().name("food").description("Food category description").build();
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(foundCategory));
        given(categoryRepository.existsCategoryByName(category.getName())).willReturn(false);
        given(categoryRepository.save(foundCategory)).willReturn(foundCategory);
        // when
        Category updatedCategory = categoryService.updateCategoryById(categoryId, category);
        // then
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> categoryNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(categoryRepository).existsCategoryByName(categoryNameCaptor.capture());
        verify(categoryRepository).save(categoryCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
        assertThat(categoryNameCaptor.getValue()).isEqualTo(category.getName());
        assertThat(categoryCaptor.getValue()).isEqualTo(category);
        assertEquals(updatedCategory, category);
    }

    @Test
    void updateCategoryByIdThrowsExceptionIfCategoryWithGivenIdDoesNotExists() {
        // given
        Long categoryId = 1L;
        Category category = Category.builder().name("food").description("Food category description").build();
        given(categoryRepository.findById(categoryId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> categoryService.updateCategoryById(categoryId, category))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Category with id = " + categoryId + " not found");
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(categoryRepository, never()).existsCategoryByName(category.getName());
        verify(categoryRepository, never()).save(category);
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
    }

    @Test
    void updateCategoryByIdThrowsExceptionIfOtherExistingCategoryHasTheSameName() {
        // given
        Long categoryId = 1L;
        Category category = Category.builder().name("food").description("Food category description").build();
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
        given(categoryRepository.existsCategoryByName(category.getName())).willReturn(true);
        // when
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> categoryNameCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> categoryService.updateCategoryById(categoryId, category))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("Category with name = " + category.getName() + " already exists");
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(categoryRepository).existsCategoryByName(categoryNameCaptor.capture());
        verify(categoryRepository, never()).save(category);
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
        assertThat(categoryNameCaptor.getValue()).isEqualTo(category.getName());
    }

    @Test
    void deleteCategoryByIdDeletesCategory() throws ObjectNotFoundException {
        // given
        Long categoryId = 1L;
        given(categoryRepository.deleteCategoryById(categoryId)).willReturn(1);
        // when
        categoryService.deleteCategoryById(categoryId);
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(categoryRepository).deleteCategoryById(categoryIdCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
    }

    @Test
    void deleteCategoryByIdThrowsExceptionIfCategoryWithGivenIdDoesNotExists() {
        // given
        Long categoryId = 1L;
        given(categoryRepository.deleteCategoryById(categoryId)).willReturn(0);
        // when
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> categoryService.deleteCategoryById(categoryId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Category with id = " + categoryId + " not found");
        verify(categoryRepository).deleteCategoryById(categoryIdCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
    }
}