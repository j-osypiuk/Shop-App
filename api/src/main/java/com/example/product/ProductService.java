package com.example.product;


import com.example.exception.DuplicateUniqueValueException;
import com.example.exception.ObjectNotFoundException;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product) throws ObjectNotFoundException, DuplicateUniqueValueException;
    Product getProductById(Long id) throws ObjectNotFoundException;
    List<Product> getAllProducts() throws ObjectNotFoundException;
    List<Product> getProductsByCategory(Long id) throws ObjectNotFoundException;
    Product updateProductById(Long id, Product product) throws ObjectNotFoundException, DuplicateUniqueValueException;
    void deleteProductById(Long id) throws ObjectNotFoundException;
}
