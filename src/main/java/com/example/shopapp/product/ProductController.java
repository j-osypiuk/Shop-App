package com.example.shopapp.product;

import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseProductDto saveProduct(@Valid @RequestBody RequestProductDto requestProductDto){
        return productService.saveProduct(requestProductDto);
    }

    @GetMapping("/{id}")
    public ResponseProductDto getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ResponseProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public ResponseProductDto updateProductById(@PathVariable("id") Long id,@Valid @RequestBody RequestProductDto requestProductDto) {
        return productService.updateProductById(requestProductDto, id);
    }

    @DeleteMapping({"/{id}"})
    public String deleteProductById(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return "Product with " + id + " deleted successfully";
    }
}
