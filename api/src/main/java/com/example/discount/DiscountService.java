package com.example.discount;


import com.example.exception.DuplicateUniqueValueException;
import com.example.exception.ObjectNotFoundException;

import java.util.List;

public interface DiscountService {

    Discount saveDiscount(Discount discount) throws DuplicateUniqueValueException;
    Discount getDiscountById(Long id) throws ObjectNotFoundException;
    List<Discount> getAllDiscounts() throws ObjectNotFoundException;
    Discount updateDiscountById(Long id, Discount discount) throws ObjectNotFoundException, DuplicateUniqueValueException;
    void deleteDiscountById(Long id) throws ObjectNotFoundException;
}
