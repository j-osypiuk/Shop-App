package com.example.shopapp.discount;

import java.util.List;
import java.util.stream.Collectors;

public class DiscountDtoMapper {

    public static DiscountDto mapDiscountToDiscountDto(Discount discount) {
        if (discount != null) {
            return new DiscountDto(
                    discount.getDiscountId(),
                    discount.getName(),
                    discount.getDescription(),
                    discount.getDiscountPercent()
            );
        }
        return null;
    }

    public static List<DiscountDto> mapDiscountListToDiscountDtoList(List<Discount> discounts) {
        return discounts.stream()
                .map(discount -> new DiscountDto(
                        discount.getDiscountId(),
                        discount.getName(),
                        discount.getDescription(),
                        discount.getDiscountPercent()
                )).collect(Collectors.toList());
    }
}
