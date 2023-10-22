package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.RequestDiscountDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @PostMapping
    public ResponseEntity<ResponseDiscountDto> saveDiscount(@Valid @RequestBody RequestDiscountDto requestDiscountDto) {
        return new ResponseEntity<>(
                discountService.saveDiscount(requestDiscountDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDiscountDto> getDiscountById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                discountService.getDiscountById(id),
                HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<List<ResponseDiscountDto>> getAllDiscounts() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                discountService.getAllDiscounts(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDiscountDto> updateDiscountById(@PathVariable("id") Long id, @Valid @RequestBody RequestDiscountDto requestDiscountDto) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                discountService.updateDiscountById(id, requestDiscountDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscountById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        discountService.deleteDiscountById(id);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
