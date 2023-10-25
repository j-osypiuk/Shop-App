package com.example.shopapp.product;

import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;

import java.util.List;

public interface ProductService {

    ResponseProductDto saveProduct(RequestProductDto requestProductDto) throws ObjectNotFoundException;
    ResponseProductDto getProductById(Long id) throws ObjectNotFoundException;
    List<ResponseProductDto> getAllProducts() throws ObjectNotFoundException;
    ResponseProductDto updateProductById(RequestProductDto requestProductDto, Long id) throws ObjectNotFoundException;
    void deleteProductById(Long id) throws ObjectNotFoundException;
}
