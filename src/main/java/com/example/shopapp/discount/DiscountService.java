package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.RequestDiscountDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;

import java.util.List;

public interface DiscountService {

    ResponseDiscountDto saveDiscount(RequestDiscountDto requestDiscountDto);
    ResponseDiscountDto getDiscountById(Long id) throws ObjectNotFoundException;
    List<ResponseDiscountDto> getAllDiscounts() throws ObjectNotFoundException;
    ResponseDiscountDto updateDiscountById(Long id, RequestDiscountDto requestDiscountDto) throws ObjectNotFoundException;
    void deleteDiscountById(Long id) throws ObjectNotFoundException;
}
