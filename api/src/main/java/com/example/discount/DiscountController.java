package com.example.discount;

import com.example.discount.dto.DiscountDtoMapper;
import com.example.discount.dto.RequestDiscountDto;
import com.example.discount.dto.ResponseDiscountDto;
import com.example.exception.DuplicateUniqueValueException;
import com.example.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> saveDiscount(@Valid @RequestBody RequestDiscountDto requestDiscountDto) throws DuplicateUniqueValueException {
        Discount discount = discountService.saveDiscount(DiscountDtoMapper.mapRequestDiscountDtoToDiscount(requestDiscountDto));

        return new ResponseEntity<>(
                Map.of("discountId", discount.getDiscountId()),
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
    public ResponseEntity<Map<String, Long>> updateDiscountById(@PathVariable("id") Long id, @Valid @RequestBody RequestDiscountDto requestDiscountDto) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Discount discount = discountService.updateDiscountById(
                id,
                DiscountDtoMapper.mapRequestDiscountDtoToDiscount(requestDiscountDto)
        );

        return new ResponseEntity<>(
                Map.of("discountId", discount.getDiscountId()),
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
