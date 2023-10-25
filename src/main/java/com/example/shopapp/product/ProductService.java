package com.example.shopapp.product;

import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;

import java.util.List;

public interface ProductService {

    ResponseProductDto saveProduct(RequestProductDto requestProductDto);
    ResponseProductDto getProductById(Long id);
    List<ResponseProductDto> getAllProducts();
    ResponseProductDto updateProductById(RequestProductDto requestProductDto, Long id);
    void deleteProductById(Long id);
}
