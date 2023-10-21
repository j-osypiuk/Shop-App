package com.example.shopapp.product.dto;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.dto.CategoryDtoMapper;
import com.example.shopapp.discount.dto.DiscountDtoMapper;
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
                        map(CategoryDtoMapper::mapCategoryToResponseCategoryDto)
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
                product.getDiscount() != null ?
                        product.getDiscount().getDiscountId() :
                        -1,
                product.getCategories().stream().
                        map(Category::getCategoryId)
                        .collect(Collectors.toList())
        );
    }

    public static OrderProductDto mapProductToOrderProductDto(Product product) {
        return new OrderProductDto(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getDiscount() != null ?
                        product.getPrice() * ((double) product.getDiscount().getDiscountPercent() / 100) :
                        0
        );
    }

    public static List<ProductDto> mapProductListToProductDtoList(List<Product> products) {
        return products.stream()
                .map(ProductDtoMapper::mapProductToProductDto)
                .collect(Collectors.toList());
    }
}
