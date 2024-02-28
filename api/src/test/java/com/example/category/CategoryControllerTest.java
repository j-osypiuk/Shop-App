package com.example.category;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CategoryController.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void saveCategoryReturnsSavedCategoryIdIfUserIsAuthenticatedAsAdmin() throws Exception {
        Category createdcategory = Category.builder()
                .categoryId(1L)
                .name("Category name")
                .description("Category description")
                .build();
        given(categoryService.saveCategory(any())).willReturn(createdcategory);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"News\",\"description\":\"News category description\"}")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(createdcategory.getCategoryId()));

        verify(categoryService).saveCategory(any());
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void saveCategoryReturnsSavedCategoryIdIfUserIsAuthenticatedAsEmployee() throws Exception {
        Category createdcategory = Category.builder()
                .categoryId(1L)
                .name("Category name")
                .description("Category description")
                .build();
        given(categoryService.saveCategory(any())).willReturn(createdcategory);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/category")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"News\",\"description\":\"News category description\"}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(createdcategory.getCategoryId()));

        verify(categoryService).saveCategory(any());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void saveCategoryReturnsForbiddenErrorIfUserIsAuthenticatedAsCustomer() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/category")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"News\",\"description\":\"News category description\"}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        verify(categoryService, never()).saveCategory(any());
    }

    @Test
    @WithAnonymousUser
    void saveCategoryReturnsUnauthorizedErrorIfUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/category")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"News\",\"description\":\"News category description\"}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(categoryService, never()).saveCategory(any());
    }

    @Test
    @WithMockUser(roles = {"ANY_ROLE"})
    void getCategoryByIdReturnsCategoryIfUserIsAuthenticated() throws Exception {
        Category category = Category.builder()
                .categoryId(1L)
                .name("Category name")
                .description("Category description")
                .build();
        given(categoryService.getCategoryById(category.getCategoryId())).willReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/" + category.getCategoryId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(category.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(category.getDescription()));

        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(categoryService).getCategoryById(categoryIdCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(category.getCategoryId());
    }

    @Test
    @WithAnonymousUser
    void getCategoryByIdReturnsUnauthorizedErrorIfUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(categoryService, never()).getCategoryById(any());
    }

    @Test
    @WithMockUser(roles = {"ANY_ROLE"})
    void getCategoryByNameReturnsCategoryIfUserIsAuthenticated() throws Exception {
        Category category = Category.builder()
                .categoryId(1L)
                .name("Fruits")
                .description("Category description")
                .build();
        given(categoryService.getCategoryByName(category.getName())).willReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category?name=" + category.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(category.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(category.getDescription()));

        ArgumentCaptor<String> categoryNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(categoryService).getCategoryByName(categoryNameCaptor.capture());
        assertThat(categoryNameCaptor.getValue()).isEqualTo(category.getName());
    }

    @Test
    @WithAnonymousUser
    void getCategoryByNameReturnsUnauthorizedErrorIfUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category?name=Fruits"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(categoryService, never()).getCategoryByName(any());
    }

    @Test
    @WithMockUser(roles = {"ANY_ROLE"})
    void getAllCategoriesReturnsCategoryIfUserIsAuthenticated() throws Exception {
        Category category1 = Category.builder()
                .categoryId(1L)
                .name("Fruits")
                .description("Fruits description")
                .build();
        Category category2 = Category.builder()
                .categoryId(2L)
                .name("Sweets")
                .description("Sweets description")
                .build();
        given(categoryService.getAllCategories()).willReturn(List.of(category1, category2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/category"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(category1.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(category1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(category1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(category2.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(category2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(category2.getDescription()));

        verify(categoryService).getAllCategories();
    }

    @Test
    @WithAnonymousUser
    void getAllCategoriesReturnsUnauthorizedErrorIfUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(categoryService, never()).getAllCategories();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updateCategoryByIdReturnsUpdatedCategoryIdIfUserIsAuthenticatedAsAdmin() throws Exception {
        Category updatedCategory = Category.builder()
                .categoryId(1L)
                .name("Category name")
                .description("Category description")
                .build();
        given(categoryService.updateCategoryById(any(), any())).willReturn(updatedCategory);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/category/" + updatedCategory.getCategoryId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Updated News\",\"description\":\"Updated News category description\"}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(updatedCategory.getCategoryId()));

        verify(categoryService).updateCategoryById(any(), any());
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    public void updateCategoryByIdReturnsUpdatedCategoryIdIfUserIsAuthenticatedAsEmployee() throws Exception {
        Category updatedCategory = Category.builder()
                .categoryId(1L)
                .name("Category name")
                .description("Category description")
                .build();
        given(categoryService.updateCategoryById(any(), any())).willReturn(updatedCategory);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/category/" + updatedCategory.getCategoryId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Updated News\",\"description\":\"Updated News category description\"}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(updatedCategory.getCategoryId()));

        verify(categoryService).updateCategoryById(any(), any());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void updateCategoryByIdReturnsForbiddenErrorIfUserIsAuthenticatedAsCustomer() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/category/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Updated News\",\"description\":\"Updated News category description\"}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        verify(categoryService, never()).updateCategoryById(any(), any());
    }

    @Test
    @WithAnonymousUser
    public void updateCategoryByIdReturnsUnauthorizedErrorIfUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/category/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Updated News\",\"description\":\"Updated News category description\"}")
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(categoryService, never()).updateCategoryById(any(), any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void deleteCategoryByIdReturnsNoContentIfUserIsAuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/category/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(categoryService).deleteCategoryById(any());
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    public void deleteCategoryByIdReturnsNoContentIfUserIsAuthenticatedAsEmployee() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/category/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(categoryService).deleteCategoryById(any());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    public void deleteCategoryByIdReturnsForbiddenErrorIfUserIsAuthenticatedAsCustomer() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/category/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        verify(categoryService, never()).deleteCategoryById(any());
    }

    @Test
    @WithAnonymousUser
    public void deleteCategoryByIdReturnsUnauthorizedErrorIfUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/category/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(categoryService, never()).deleteCategoryById(any());
    }
}