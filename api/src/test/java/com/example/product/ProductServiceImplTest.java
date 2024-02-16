package com.example.product;

import com.example.category.Category;
import com.example.category.CategoryRepository;
import com.example.discount.Discount;
import com.example.discount.DiscountRepository;
import com.example.exception.DuplicateUniqueValueException;
import com.example.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private CategoryRepository categoryRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, discountRepository, categoryRepository);
    }

    @Test
    void saveProductWithDiscountSavesProduct() throws ObjectNotFoundException, DuplicateUniqueValueException {
        // given
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Discount foundDiscount = Discount.builder()
                .discountId(1L)
                .name("Summer discount")
                .description("Summer discount description")
                .discountPercent(15)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Category foundCategory = Category.builder()
                .categoryId(1L)
                .name("Drinks")
                .description("Drink category description")
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(discountRepository.findById(discount.getDiscountId())).willReturn(Optional.of(foundDiscount));
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.of(foundCategory));
        given(productRepository.save(product)).willReturn(product);
        // when
        Product savedProduct = productService.saveProduct(product);
        // then
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(productRepository).save(productCaptor.capture());
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(discountIdCaptor.getValue()).isEqualTo(product.getDiscount().getDiscountId());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(product.getCategories().get(0).getCategoryId());
        assertNotNull(savedProduct);
        assertEquals(savedProduct.getDiscount().getDiscountId(), product.getDiscount().getDiscountId());
        assertEquals(savedProduct.getCategories().get(0).getCategoryId(), product.getCategories().get(0).getCategoryId());
    }

    @Test
    void saveProductWithoutDiscountSavesProduct() throws ObjectNotFoundException, DuplicateUniqueValueException {
        // given
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Category foundCategory = Category.builder()
                .categoryId(1L)
                .name("Drinks")
                .description("Drink category description")
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.of(foundCategory));
        given(productRepository.save(product)).willReturn(product);
        // when
        Product savedProduct = productService.saveProduct(product);
        // then
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository, never()).findById(any());
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(productRepository).save(productCaptor.capture());
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(product.getCategories().get(0).getCategoryId());
        assertNotNull(savedProduct);
        assertNull(savedProduct.getDiscount());
        assertEquals(savedProduct.getCategories().get(0).getCategoryId(), product.getCategories().get(0).getCategoryId());
    }

    @Test
    void saveProductThrowsExceptionIfOtherExistingProductHasTheSameName() {
        // given
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .build();
        given(productRepository.existsProductByName(product.getName())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> productService.saveProduct(product))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("Product with name = " + product.getName() + " already exists");
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository, never()).findById(any());
        verify(categoryRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
    }

    @Test
    void saveProductThrowsExceptionIfProductDiscountDoesNotExists() {
        // given
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .build();
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(discountRepository.findById(discount.getDiscountId())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> productService.saveProduct(product))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Discount with id = " + product.getDiscount().getDiscountId() + " not found");
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(categoryRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(discountIdCaptor.getValue()).isEqualTo(product.getDiscount().getDiscountId());
    }

    @Test
    void saveProductThrowsExceptionIfOneOfProductCategoriesDoesNotExists() {
        // given
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Discount foundDiscount = Discount.builder()
                .discountId(1L)
                .name("Summer discount")
                .description("Summer discount description")
                .discountPercent(15)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(discountRepository.findById(discount.getDiscountId())).willReturn(Optional.of(foundDiscount));
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> productService.saveProduct(product))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Category with id = " + category.getCategoryId() + " not found");
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(productRepository, never()).save(any());
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(discountIdCaptor.getValue()).isEqualTo(product.getDiscount().getDiscountId());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(product.getCategories().get(0).getCategoryId());
    }

    @Test
    void getProductByIdReturnsProduct() throws ObjectNotFoundException {
        // given
        Long productId = 1L;
        Product product = Product.builder().productId(productId).build();
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        // when
        Product foundProduct = productService.getProductById(productId);
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).findById(productIdCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
        assertEquals(foundProduct, product);
    }

    @Test
    void getProductByIdThrowsExceptionIfProductDoesNotExists() {
        // given
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Product with id = " + productId + " not found");
        verify(productRepository).findById(productIdCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
    }

    @Test
    void getAllProductsReturnsListOfProducts() throws ObjectNotFoundException {
        // given
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Product product1 = Product.builder()
                                .name("Orange juice")
                                .description("Orange juice description")
                                .categories(Arrays.asList(category))
                                .build();
        Product product2 = Product.builder()
                                .name("Melons")
                                .description("Melons description")
                                .categories(Arrays.asList(category))
                                .build();
        given(productRepository.findAllByCategoryId(category.getCategoryId())).willReturn(Arrays.asList(product1, product2));
        // when
        List<Product> products = productService.getProductsByCategory(category.getCategoryId());
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).findAllByCategoryId(categoryIdCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(category.getCategoryId());
        assertEquals(products.size(), 2);
        assertEquals(products.get(0), product1);
        assertEquals(products.get(1), product2);
    }

    @Test
    void getAllProductsThrowsExceptionIfNoProductsExists() {
        // given
        Long categoryId = 999L;
        given(productRepository.findAllByCategoryId(categoryId)).willReturn(Collections.emptyList());
        // when
        // then
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> productService.getProductsByCategory(categoryId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No products found");
        verify(productRepository).findAllByCategoryId(categoryIdCaptor.capture());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryId);
    }

    @Test
    void getProductsByCategoryReturnsListOfProducts() throws ObjectNotFoundException {
        // given
        Product product1 = Product.builder().name("Orange juice").description("Orange juice description").build();
        Product product2 = Product.builder().name("Melons").description("Melons description").build();
        given(productRepository.findAll()).willReturn(Arrays.asList(product1, product2));
        // when
        List<Product> products = productService.getAllProducts();
        // then
        verify(productRepository).findAll();
        assertEquals(products.size(), 2);
        assertEquals(products.get(0), product1);
        assertEquals(products.get(1), product2);
    }

    @Test
    void getProductsByCategoryExceptionIfNoProductsExists() {
        // given
        given(productRepository.findAll()).willReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(() -> productService.getAllProducts())
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No products found");
        verify(productRepository).findAll();
    }

    @Test
    void updateProductByIdUpdatesProductWithDiscount() throws ObjectNotFoundException, DuplicateUniqueValueException {
        // given
        Long productId = 1L;
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Discount foundDiscount = Discount.builder()
                .discountId(1L)
                .name("Summer discount")
                .description("Summer discount description")
                .discountPercent(15)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Category foundCategory = Category.builder()
                .categoryId(1L)
                .name("Drinks")
                .description("Drink category description")
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        Product foundProduct = Product.builder()
                .productId(1L)
                .name("Apple juice")
                .description("Apple juice description")
                .price(10)
                .amount(25)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.findById(productId)).willReturn(Optional.of(foundProduct));
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(discountRepository.findById(discount.getDiscountId())).willReturn(Optional.of(foundDiscount));
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.of(foundCategory));
        given(productRepository.save(foundProduct)).willReturn(foundProduct);
        // when
        Product updatedProduct = productService.updateProductById(productId, product);
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).findById(productIdCaptor.capture());
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(productRepository).save(productCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(discountIdCaptor.getValue()).isEqualTo(product.getDiscount().getDiscountId());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(product.getCategories().get(0).getCategoryId());
        assertThat(productCaptor.getValue()).isEqualTo(foundProduct);
        assertNotNull(updatedProduct);
        assertEquals(updatedProduct.getDiscount().getDiscountId(), product.getDiscount().getDiscountId());
        assertEquals(updatedProduct.getCategories().get(0).getCategoryId(), product.getCategories().get(0).getCategoryId());
    }

    @Test
    void updateProductByIdUpdatesProductWithoutDiscount() throws ObjectNotFoundException, DuplicateUniqueValueException {
        // given
        Long productId = 1L;
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Category foundCategory = Category.builder()
                .categoryId(1L)
                .name("Drinks")
                .description("Drink category description")
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(null)
                .categories(Collections.singletonList(category))
                .build();
        Product foundProduct = Product.builder()
                .productId(1L)
                .name("Apple juice")
                .description("Apple juice description")
                .price(10)
                .amount(25)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.findById(productId)).willReturn(Optional.of(foundProduct));
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.of(foundCategory));
        given(productRepository.save(foundProduct)).willReturn(foundProduct);
        // when
        Product updatedProduct = productService.updateProductById(productId, product);
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).findById(productIdCaptor.capture());
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository, never()).findById(any());
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(productRepository).save(productCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(product.getCategories().get(0).getCategoryId());
        assertThat(productCaptor.getValue()).isEqualTo(foundProduct);
        assertNotNull(updatedProduct);
        assertNull(updatedProduct.getDiscount());
        assertEquals(updatedProduct.getCategories().get(0).getCategoryId(), product.getCategories().get(0).getCategoryId());
    }

    @Test
    void updateProductByIdThrowsExceptionIfProductWithGivenIdDoesNotExists() {
        // given
        Long productId = 1L;
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.findById(productId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> productService.updateProductById(productId, product))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Product with id = " + productId + " not found");
        verify(productRepository).findById(productIdCaptor.capture());
        verify(productRepository, never()).existsProductByName(any());
        verify(discountRepository, never()).findById(any());
        verify(categoryRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
    }

    @Test
    void updateProductByIdThrowsExceptionIfOtherExistingProductHasTheSameName() {
        // given
        Long productId = 1L;
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        Product foundProduct = Product.builder()
                .productId(1L)
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.findById(productId)).willReturn(Optional.of(foundProduct));
        given(productRepository.existsProductByName(product.getName())).willReturn(true);
        // when
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> productService.updateProductById(productId, product))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("Product with name = " + product.getName() + " already exists");
        verify(productRepository).findById(productIdCaptor.capture());
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository, never()).findById(any());
        verify(categoryRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
    }

    @Test
    void updateProductByIdThrowsExceptionIfProductDiscountDoesNotExists() {
        // given
        Long productId = 1L;
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        Product foundProduct = Product.builder()
                .productId(1L)
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.findById(productId)).willReturn(Optional.of(foundProduct));
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(discountRepository.findById(discount.getDiscountId())).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> productService.updateProductById(productId, product))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Discount with id = " + product.getDiscount().getDiscountId() + " not found");
        verify(productRepository).findById(productIdCaptor.capture());
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(categoryRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(discountIdCaptor.getValue()).isEqualTo(product.getDiscount().getDiscountId());
    }

    @Test
    void updateProductByIdThrowsExceptionIfOneOfProductCategoriesDoesNotExists() {
        // given
        Long productId = 1L;
        Discount discount = Discount.builder()
                .discountId(1L)
                .build();
        Discount foundDiscount = Discount.builder()
                .discountId(1L)
                .name("Summer discount")
                .description("Summer discount description")
                .discountPercent(15)
                .build();
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Product product = Product.builder()
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        Product foundProduct = Product.builder()
                .productId(1L)
                .name("Orange juice")
                .description("Orange juice description")
                .price(12.5)
                .amount(15)
                .discount(discount)
                .categories(Collections.singletonList(category))
                .build();
        given(productRepository.findById(productId)).willReturn(Optional.of(foundProduct));
        given(productRepository.existsProductByName(product.getName())).willReturn(false);
        given(discountRepository.findById(discount.getDiscountId())).willReturn(Optional.of(foundDiscount));
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> productNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> discountIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> productService.updateProductById(productId, product))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Category with id = " + product.getDiscount().getDiscountId() + " not found");
        verify(productRepository).findById(productIdCaptor.capture());
        verify(productRepository).existsProductByName(productNameCaptor.capture());
        verify(discountRepository).findById(discountIdCaptor.capture());
        verify(categoryRepository).findById(categoryIdCaptor.capture());
        verify(productRepository, never()).save(any());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
        assertThat(productNameCaptor.getValue()).isEqualTo(product.getName());
        assertThat(discountIdCaptor.getValue()).isEqualTo(product.getDiscount().getDiscountId());
        assertThat(categoryIdCaptor.getValue()).isEqualTo(product.getCategories().get(0).getCategoryId());
    }

    @Test
    void deleteProductByIdDeletesProduct() throws ObjectNotFoundException {
        // given
        Long productId = 1L;
        given(productRepository.deleteProductById(productId)).willReturn(1);
        // when
        productService.deleteProductById(productId);
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(productRepository).deleteProductById(productIdCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
    }

    @Test
    void deleteProductByIdThrowsExceptionIfProductWithGivenIdDoesNotExists() {
        // given
        Long productId = 1L;
        given(productRepository.deleteProductById(productId)).willReturn(0);
        // when
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> productService.deleteProductById(productId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Product with id = " + productId + " not found");
        verify(productRepository).deleteProductById(productIdCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
    }
}