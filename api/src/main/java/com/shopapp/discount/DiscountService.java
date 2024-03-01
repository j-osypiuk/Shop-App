package com.shopapp.discount;


import com.shopapp.exception.DuplicateUniqueValueException;
import com.shopapp.exception.ObjectNotFoundException;

import java.util.List;

public interface DiscountService {

    Discount saveDiscount(Discount discount) throws DuplicateUniqueValueException;
    Discount getDiscountById(Long id) throws ObjectNotFoundException;
    List<Discount> getAllDiscounts() throws ObjectNotFoundException;
    Discount updateDiscountById(Long id, Discount discount) throws ObjectNotFoundException, DuplicateUniqueValueException;
    void deleteDiscountById(Long id) throws ObjectNotFoundException;
}
