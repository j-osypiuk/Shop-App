package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.RequestDiscountDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;

import java.util.List;

public interface DiscountService {

    ResponseDiscountDto saveDiscount(RequestDiscountDto requestDiscountDto);
    ResponseDiscountDto getDiscountById(Long id);
    List<ResponseDiscountDto> getAllDiscounts();
    ResponseDiscountDto updateDiscountById(Long id, RequestDiscountDto requestDiscountDto);
    void deleteDiscountById(Long id);
}
