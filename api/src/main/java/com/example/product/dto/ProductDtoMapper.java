package com.example.product.dto;

import com.example.category.Category;
import com.example.category.dto.CategoryDtoMapper;
import com.example.discount.Discount;
import com.example.discount.dto.DiscountDtoMapper;
import com.example.product.Product;

import java.util.List;

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
                            .toList())
                .build();
    }

    public static ResponseProductDto mapProductToResponseProductDto(Product product) {
        return ResponseProductDto.builder()
                .id(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .amount(product.getAmount())
                .price(product.getPrice())
                .discount(DiscountDtoMapper.mapDiscountToResponseDiscountDto(product.getDiscount()))
                .categories(product.getCategories().stream()
                        .map(CategoryDtoMapper::mapCategoryToResponseCategoryDto)
                        .toList())
                .productPhotos(product.getProductPhotos() != null ? product.getProductPhotos().stream()
                                .map(productPhoto -> productPhoto.getPhotoName().toString() + ".png").toList() : null)
                .build();
    }

    public static List<ResponseProductDto> mapProductListToProductDtoList(List<Product> products) {
        return products.stream()
                .map(ProductDtoMapper::mapProductToResponseProductDto)
                .toList();
    }
}
