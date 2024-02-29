package com.example.product;

import com.example.exception.DuplicateUniqueValueException;
import com.example.exception.ObjectNotFoundException;
import com.example.product.dto.ProductDtoMapper;
import com.example.product.dto.RequestProductDto;
import com.example.product.dto.ResponseProductDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> saveProduct(@Valid @RequestBody RequestProductDto requestProductDto) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Product product = productService.saveProduct(ProductDtoMapper.mapRequestProductDtoToProduct(requestProductDto));

        return new ResponseEntity<>(
                Map.of("productId", product.getProductId()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDto> getProductById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                ProductDtoMapper.mapProductToResponseProductDto(productService.getProductById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseProductDto>> getAllProducts() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                ProductDtoMapper.mapProductListToProductDtoList(productService.getAllProducts()),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "category_id")
    public ResponseEntity<List<ResponseProductDto>> getAllByCategory(@RequestParam("category_id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                ProductDtoMapper.mapProductListToProductDtoList(productService.getProductsByCategory(id)),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Long>> updateProductById(@PathVariable("id") Long id, @Valid @RequestBody RequestProductDto requestProductDto) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Product product = productService.updateProductById(id, ProductDtoMapper.mapRequestProductDtoToProduct(requestProductDto));

        return new ResponseEntity<>(
                Map.of("productId", product.getProductId()),
                HttpStatus.OK
        );
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
