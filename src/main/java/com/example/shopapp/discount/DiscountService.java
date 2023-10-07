package com.example.shopapp.discount;

import java.util.List;

public interface DiscountService {

    DiscountDto saveDiscount(Discount discount);
    DiscountDto getDiscountById(Long id);
    List<DiscountDto> getAllDiscounts();
    DiscountDto updateDiscountById(Long id, Discount discount);
    void deleteDiscountById(Long id);
}
