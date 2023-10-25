package com.example.shopapp.product;

import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
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
    public ResponseEntity<ResponseProductDto> updateProductById(@PathVariable("id") Long id,@Valid @RequestBody RequestProductDto requestProductDto) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                productService.updateProductById(requestProductDto, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
