package com.example.shopapp.product;

import com.example.shopapp.category.Category;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.product.dto.PostProductDto;
import com.example.shopapp.product.dto.ProductDto;
import com.example.shopapp.product.dto.ProductDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public PostProductDto saveProduct(Product product) {
        Product productDB = productRepository.save(product);
        return ProductDtoMapper.mapProductToPostProductDto(productDB);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product productDB = productRepository.findById(id).get();
        productDB.getCategories().stream().map(Category::getName).collect(Collectors.toList());
        return ProductDtoMapper.mapProductToProductDto(productDB);
        //        return productRepository.findById(id).get();
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductDtoMapper.mapProductListToProductDtoList(products);
    }

    @Override
    public ProductDto updateProductById(Product product, Long id) {
        Product productDB = productRepository.findById(id).get();

        if (product.getName() != null && !product.getName().isEmpty()) productDB.setName(product.getName());
        if (product.getDescription() != null && !product.getDescription().isEmpty()) productDB.setDescription(product.getDescription());
        if (product.getAmount() >= 0) productDB.setAmount(product.getAmount());
        if (product.getPrice() > 0) productDB.setPrice(product.getPrice());
        if (product.getDiscount() != null) {
            if (product.getDiscount().getDiscountId() == null) productDB.setDiscount(null);
            else if (product.getDiscount().getDiscountId() > 0) productDB.setDiscount(Discount.builder().discountId(product.getDiscount().getDiscountId()).build());
        }
        if (product.getCategories() != null) {
            productDB.setCategories(product.getCategories());
        }

        Product updatedProduct = productRepository.save(productDB);
        return ProductDtoMapper.mapProductToProductDto(updatedProduct);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
