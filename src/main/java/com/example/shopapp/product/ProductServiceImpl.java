package com.example.shopapp.product;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.CategoryRepository;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.discount.DiscountRepository;
import com.example.shopapp.product.dto.ProductDtoMapper;
import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ResponseProductDto saveProduct(RequestProductDto requestProductDto) {
        Product productx = ProductDtoMapper.mapRequestProductDtoToProduct(requestProductDto);
        Product productDB = productRepository.save(productx);


        if (requestProductDto.discountId() != null){
            Optional<Discount> discountDB = discountRepository.findById(requestProductDto.discountId());
            if (discountDB.isPresent()) productDB.setDiscount(discountDB.get());
        }

        List<Category> categories = new ArrayList<>();
        for (Long id : requestProductDto.categoryIds()) {
            Optional<Category> categoryDB = categoryRepository.findById(id);
            if (categoryDB.isPresent()) categories.add(categoryDB.get());
        }

        productDB.setCategories(categories);

        return ProductDtoMapper.mapProductToResponseProductDto(productDB);
    }

    @Override
    public ResponseProductDto getProductById(Long id) {
        Product productDB = productRepository.findById(id).get();
        return ProductDtoMapper.mapProductToResponseProductDto(productDB);
    }

    @Override
    public List<ResponseProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductDtoMapper.mapProductListToProductDtoList(products);
    }

    @Override
    public ResponseProductDto updateProductById(RequestProductDto requestProductDto, Long id) {
//        Product productDB = productRepository.findById(id).get();
//
//        if (product.getName() != null && !product.getName().isEmpty()) productDB.setName(product.getName());
//        if (product.getDescription() != null && !product.getDescription().isEmpty()) productDB.setDescription(product.getDescription());
//        if (product.getAmount() >= 0) productDB.setAmount(product.getAmount());
//        if (product.getPrice() > 0) productDB.setPrice(product.getPrice());
//        if (product.getDiscount() != null) {
//            if (product.getDiscount().getDiscountId() == null) productDB.setDiscount(null);
//            else if (product.getDiscount().getDiscountId() > 0) productDB.setDiscount(Discount.builder().discountId(product.getDiscount().getDiscountId()).build());
//        }
//        if (product.getCategories() != null) {
//            productDB.setCategories(product.getCategories());
//        }
//
//        Product updatedProduct = productRepository.save(productDB);
//        return ProductDtoMapper.mapProductToResponseProductDto(updatedProduct);
        return null;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
