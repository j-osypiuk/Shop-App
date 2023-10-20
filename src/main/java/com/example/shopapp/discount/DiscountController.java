package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.DiscountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @PostMapping
    public DiscountDto saveDiscount(@RequestBody Discount discount) {
        return discountService.saveDiscount(discount);
    }

    @GetMapping("/{id}")
    public DiscountDto getDiscountById(@PathVariable("id") Long id) {
        return discountService.getDiscountById(id);
    }

    @GetMapping()
    public List<DiscountDto> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @PutMapping("/{id}")
    public DiscountDto updateDiscountById(@PathVariable("id") Long id, @RequestBody Discount discount) {
        return discountService.updateDiscountById(id, discount);
    }

    @DeleteMapping("/{id}")
    public String deleteDiscountById(@PathVariable("id") Long id) {
        discountService.deleteDiscountById(id);
        return "Discount with id " + id + " deleted successfully";
    }
}
