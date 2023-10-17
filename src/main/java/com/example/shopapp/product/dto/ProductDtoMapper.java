package com.example.shopapp.product.dto;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.CategoryDtoMapper;
import com.example.shopapp.discount.DiscountDtoMapper;
import com.example.shopapp.product.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDtoMapper {

    public static ProductDto mapProductToProductDto(Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getAmount(),
                product.getPrice(),
                DiscountDtoMapper.mapDiscountToDiscountDto(product.getDiscount()),
                product.getCategories().stream().
                        map(CategoryDtoMapper::mapCategoryToCategoryDto)
                        .collect(Collectors.toList()),
                product.getProductPhotos().stream()
                        .map(productPhoto -> productPhoto.getPhotoName().toString() + ".png")
                        .collect(Collectors.toList())
        );
    }

    public static PostProductDto mapProductToPostProductDto(Product product) {
        return new PostProductDto(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getAmount(),
                product.getPrice(),
                DiscountDtoMapper.mapDiscountToDiscountDto(product.getDiscount()),
                product.getCategories().stream().
                        map(Category::getCategoryId)
                        .collect(Collectors.toList())
        );
    }

    public static List<ProductDto> mapProductListToProductDtoList(List<Product> products) {
        return products.stream()
                .map(ProductDtoMapper::mapProductToProductDto)
                .collect(Collectors.toList());
    }
}
