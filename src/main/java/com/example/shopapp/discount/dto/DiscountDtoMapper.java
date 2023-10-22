package com.example.shopapp.discount.dto;

import com.example.shopapp.discount.Discount;

import java.util.List;
import java.util.stream.Collectors;

public class DiscountDtoMapper {

    public static ResponseDiscountDto mapDiscountToResponseDiscountDto(Discount discount) {
        if (discount != null) {
            return new ResponseDiscountDto(
                    discount.getDiscountId(),
                    discount.getName(),
                    discount.getDescription(),
                    discount.getDiscountPercent()
            );
        }
        return null;
    }

    public static Discount mapRequestDiscountDtoToDiscount(RequestDiscountDto requestDiscountDto) {
        return Discount.builder()
                .name(requestDiscountDto.name())
                .description(requestDiscountDto.description())
                .build();
    }

    public static List<ResponseDiscountDto> mapDiscountListToResponseDiscountDtoList(List<Discount> discounts) {
        return discounts.stream()
                .map(discount -> new ResponseDiscountDto(
                        discount.getDiscountId(),
                        discount.getName(),
                        discount.getDescription(),
                        discount.getDiscountPercent()
                )).collect(Collectors.toList());
    }
}
