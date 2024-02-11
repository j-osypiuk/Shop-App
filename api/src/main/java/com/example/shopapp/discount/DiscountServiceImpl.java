package com.example.shopapp.discount;

import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService{

    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount saveDiscount(Discount discount) throws DuplicateUniqueValueException {
        if (discountRepository.existsDiscountByName(discount.getName()))
            throw new DuplicateUniqueValueException("Discount with name = " + discount.getName() + " already exists");

        return discountRepository.save(discount);
    }

    @Override
    public Discount getDiscountById(Long id) throws ObjectNotFoundException {
        return discountRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + id + " not found"));
    }

    @Override
    public List<Discount> getAllDiscounts() throws ObjectNotFoundException {
        List<Discount> discountsDB = discountRepository.findAll();

        if (discountsDB.isEmpty()) throw new ObjectNotFoundException("No discounts found");

        return discountsDB;
    }

    @Override
    public Discount updateDiscountById(Long id, Discount discount) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Discount discountDB = discountRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + id + " not found"));

        if (discountRepository.existsDiscountByName(discount.getName()))
            throw new DuplicateUniqueValueException("Discount with name = " + discount.getName() + " already exists");

        if (!discount.getName().equals(discountDB.getName()))
            discountDB.setName(discount.getName());
        if (!discount.getDescription().equals(discountDB.getDescription()))
            discountDB.setDescription(discount.getDescription());
        if (discount.getDiscountPercent() != discountDB.getDiscountPercent())
            discountDB.setDiscountPercent(discount.getDiscountPercent());

        return discountRepository.save(discountDB);
    }

    @Override
    public void deleteDiscountById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = discountRepository.deleteDiscountById(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Discount with id = " + id + " not found");
    }
}
