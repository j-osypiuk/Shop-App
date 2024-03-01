package com.shopapp.discount.dto;

import com.shopapp.discount.Discount;

import java.util.List;

public class DiscountDtoMapper {

    public static ResponseDiscountDto mapDiscountToResponseDiscountDto(Discount discount) {
        if (discount != null) {
            return ResponseDiscountDto.builder()
                    .id(discount.getDiscountId())
                    .name(discount.getName())
                    .description(discount.getDescription())
                    .discountPercent(discount.getDiscountPercent())
                    .build();
        }
        return null;
    }

    public static Discount mapRequestDiscountDtoToDiscount(RequestDiscountDto requestDiscountDto) {
        return Discount.builder()
                .name(requestDiscountDto.name())
                .description(requestDiscountDto.description())
                .discountPercent(requestDiscountDto.discountPercent())
                .build();
    }

    public static List<ResponseDiscountDto> mapDiscountListToResponseDiscountDtoList(List<Discount> discounts) {
        return discounts.stream()
                .map(DiscountDtoMapper::mapDiscountToResponseDiscountDto)
                .toList();
    }
}
