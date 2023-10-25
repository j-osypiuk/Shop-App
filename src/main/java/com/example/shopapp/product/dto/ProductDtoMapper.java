package com.example.shopapp.product.dto;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.dto.CategoryDtoMapper;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.discount.dto.DiscountDtoMapper;
import com.example.shopapp.product.Product;

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
                .categories(!requestProductDto.categoryIds().isEmpty() ?
                            requestProductDto.categoryIds().stream()
                            .map(id -> Category.builder().categoryId(id).build())
                            .collect(Collectors.toList()) :
                            null)
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

    public static List<ResponseProductDto> mapProductListToProductDtoList(List<Product> products) {
        return products.stream()
                .map(ProductDtoMapper::mapProductToResponseProductDto)
                .collect(Collectors.toList());
    }
}
