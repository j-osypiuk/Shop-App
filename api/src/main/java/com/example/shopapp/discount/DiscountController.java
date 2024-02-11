package com.example.shopapp.discount;

import com.example.shopapp.discount.dto.DiscountDtoMapper;
import com.example.shopapp.discount.dto.RequestDiscountDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;
import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    public ResponseEntity<ResponseDiscountDto> saveDiscount(@Valid @RequestBody RequestDiscountDto requestDiscountDto) throws DuplicateUniqueValueException {
        Discount discount = discountService.saveDiscount(DiscountDtoMapper.mapRequestDiscountDtoToDiscount(requestDiscountDto));

        return new ResponseEntity<>(
                DiscountDtoMapper.mapDiscountToResponseDiscountDto(discount),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDiscountDto> getDiscountById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                DiscountDtoMapper.mapDiscountToResponseDiscountDto(discountService.getDiscountById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<List<ResponseDiscountDto>> getAllDiscounts() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                DiscountDtoMapper.mapDiscountListToResponseDiscountDtoList(discountService.getAllDiscounts()),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDiscountDto> updateDiscountById(@PathVariable("id") Long id, @Valid @RequestBody RequestDiscountDto requestDiscountDto) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Discount discount = discountService.updateDiscountById(
                id,
                DiscountDtoMapper.mapRequestDiscountDtoToDiscount(requestDiscountDto)
        );

        return new ResponseEntity<>(
                DiscountDtoMapper.mapDiscountToResponseDiscountDto(discount),
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
