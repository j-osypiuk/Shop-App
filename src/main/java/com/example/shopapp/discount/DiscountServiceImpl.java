package com.example.shopapp.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService{

    @Autowired
    DiscountRepository discountRepository;

    @Override
    public DiscountDto saveDiscount(Discount discount) {
        Discount discountDB = discountRepository.save(discount);
        return DiscountDtoMapper.mapDiscountToDiscountDto(discountDB);
    }

    @Override
    public DiscountDto getDiscountById(Long id) {
        Discount discountDB = discountRepository.findById(id).get();
        return DiscountDtoMapper.mapDiscountToDiscountDto(discountDB);
    }

    @Override
    public List<DiscountDto> getAllDiscounts() {
        List<Discount> discountsDB = discountRepository.findAll();
        return DiscountDtoMapper.mapDiscountListToDiscountDtoList(discountsDB);
    }

    @Override
    public DiscountDto updateDiscountById(Long id, Discount discount) {
        Discount discountDB = discountRepository.findById(id).get();

        if (discount.getName() != null && !discount.getName().isEmpty()) discountDB.setName(discount.getName());
        if (discount.getDescription() != null && !discount.getDescription().isEmpty()) discountDB.setDescription(discount.getDescription());
        if (discount.getDiscountPercent() >  0) discountDB.setDiscountPercent(discount.getDiscountPercent());

        Discount updatedDiscount = discountRepository.save(discountDB);
        return DiscountDtoMapper.mapDiscountToDiscountDto(updatedDiscount);
    }

    @Override
    public void deleteDiscountById(Long id) {
        discountRepository.deleteById(id);
    }
}
