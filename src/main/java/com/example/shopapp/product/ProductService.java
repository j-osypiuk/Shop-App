package com.example.shopapp.product;

import com.example.shopapp.product.dto.PostProductDto;
import com.example.shopapp.product.dto.ProductDto;

import java.util.List;

public interface ProductService {

    PostProductDto saveProduct(Product product);
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts();
    ProductDto updateProductById(Product product, Long id);
    void deleteProductById(Long id);
}
