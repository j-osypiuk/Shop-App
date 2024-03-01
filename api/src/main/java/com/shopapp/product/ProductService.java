package com.shopapp.product;


import com.shopapp.exception.DuplicateUniqueValueException;
import com.shopapp.exception.ObjectNotFoundException;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product) throws ObjectNotFoundException, DuplicateUniqueValueException;
    Product getProductById(Long id) throws ObjectNotFoundException;
    List<Product> getAllProducts() throws ObjectNotFoundException;
    List<Product> getProductsByCategory(Long id) throws ObjectNotFoundException;
    Product updateProductById(Long id, Product product) throws ObjectNotFoundException, DuplicateUniqueValueException;
    void deleteProductById(Long id) throws ObjectNotFoundException;
}
