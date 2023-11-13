package com.example.shopapp.discount;

import com.example.shopapp.exception.ObjectNotFoundException;

import java.util.List;

public interface DiscountService {

    Discount saveDiscount(Discount discount);
    Discount getDiscountById(Long id) throws ObjectNotFoundException;
    List<Discount> getAllDiscounts() throws ObjectNotFoundException;
    Discount updateDiscountById(Long id, Discount discount) throws ObjectNotFoundException;
    void deleteDiscountById(Long id) throws ObjectNotFoundException;
}
