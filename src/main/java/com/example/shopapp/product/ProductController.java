package com.example.shopapp.product;

import com.example.shopapp.product.dto.PostProductDto;
import com.example.shopapp.product.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public PostProductDto saveProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public ProductDto updateProductById(@PathVariable("id") Long id, @RequestBody Product product) {
        return productService.updateProductById(product, id);
    }

    @DeleteMapping({"/{id}"})
    public String deleteProductById(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return "Product with " + id + " deleted successfully";
    }
}
