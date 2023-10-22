package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.RequestDiscountDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @PostMapping
    public ResponseDiscountDto saveDiscount(@RequestBody RequestDiscountDto requestDiscountDto) {
        return discountService.saveDiscount(requestDiscountDto);
    }

    @GetMapping("/{id}")
    public ResponseDiscountDto getDiscountById(@PathVariable("id") Long id) {
        return discountService.getDiscountById(id);
    }

    @GetMapping()
    public List<ResponseDiscountDto> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @PutMapping("/{id}")
    public ResponseDiscountDto updateDiscountById(@PathVariable("id") Long id, @RequestBody RequestDiscountDto requestDiscountDto) {
        return discountService.updateDiscountById(id, requestDiscountDto);
    }

    @DeleteMapping("/{id}")
    public String deleteDiscountById(@PathVariable("id") Long id) {
        discountService.deleteDiscountById(id);
        return "Discount with id " + id + " deleted successfully";
    }
}
