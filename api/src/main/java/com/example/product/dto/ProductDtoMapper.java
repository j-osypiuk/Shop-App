package com.example.product.dto;

import com.example.category.Category;
import com.example.category.dto.CategoryDtoMapper;
import com.example.discount.Discount;
import com.example.discount.dto.DiscountDtoMapper;
import com.example.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDtoMapper {

    public static Product mapRequestProductDtoToProduct(RequestProductDto requestProductDto) {
        return Product.builder()
                .name(requestProductDto.name())
                .description(requestProductDto.description())
                .amount(requestProductDto.amount())
                .price(requestProductDto.price())
                .discount(requestProductDto.discountId() != null ?
                        Discount.builder().discountId(requestProductDto.discountId()).build() :
                        null)
                .categories(requestProductDto.categoryIds().stream()
                            .map(id -> Category.builder().categoryId(id).build())
                            .collect(Collectors.toList()))
                .build();
    }

    public static ResponseProductDto mapProductToResponseProductDto(Product product) {
        return new ResponseProductDto(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getAmount(),
                product.getPrice(),
                DiscountDtoMapper.mapDiscountToResponseDiscountDto(product.getDiscount()),
                product.getCategories().stream().
                        map(CategoryDtoMapper::mapCategoryToResponseCategoryDto)
                        .collect(Collectors.toList()),
                product.getProductPhotos() != null ?
                        product.getProductPhotos().stream()
                        .map(productPhoto -> productPhoto.getPhotoName().toString() + ".png")
                        .collect(Collectors.toList()) :
                        new ArrayList<>()
        );
    }

    public static List<ResponseProductDto> mapProductListToProductDtoList(List<Product> products) {
        return products.stream()
                .map(ProductDtoMapper::mapProductToResponseProductDto)
                .collect(Collectors.toList());
    }
}
