package com.example.shopapp.product;

import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<ResponseProductDto> saveProduct(@Valid @RequestBody RequestProductDto requestProductDto) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                productService.saveProduct(requestProductDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProductById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                productService.getProductById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseProductDto>> getAllProducts() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                productService.getAllProducts(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<ResponseProductDto> updateProductById(@PathVariable("id") Long id,@Valid @RequestBody RequestProductDto requestProductDto) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                productService.updateProductById(requestProductDto, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping({"/{id}"})
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
