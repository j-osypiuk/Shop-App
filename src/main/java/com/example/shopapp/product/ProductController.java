package com.example.shopapp.product;

import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.product.dto.ProductDtoMapper;
import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseProductDto> saveProduct(@Valid @RequestBody RequestProductDto requestProductDto) throws ObjectNotFoundException {
        Product product = productService.saveProduct(ProductDtoMapper.mapRequestProductDtoToProduct(requestProductDto));

        return new ResponseEntity<>(
                ProductDtoMapper.mapProductToResponseProductDto(product),
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

    @PutMapping("/{id}")
    public ResponseEntity<ResponseProductDto> updateProductById(@PathVariable("id") Long id, @Valid @RequestBody RequestProductDto requestProductDto) throws ObjectNotFoundException {
        Product product = productService.updateProductById(id, ProductDtoMapper.mapRequestProductDtoToProduct(requestProductDto));

        return new ResponseEntity<>(
                ProductDtoMapper.mapProductToResponseProductDto(product),
                HttpStatus.OK
        );
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
