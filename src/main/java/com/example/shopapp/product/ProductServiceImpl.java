package com.example.shopapp.product;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.CategoryRepository;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.discount.DiscountRepository;
import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.product.dto.ProductDtoMapper;
import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              DiscountRepository discountRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseProductDto saveProduct(RequestProductDto requestProductDto) throws ObjectNotFoundException {
        Product productDB = new Product();

        if (requestProductDto.discountId() != null){
            Discount discountDB = discountRepository.findById(requestProductDto.discountId())
                    .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + requestProductDto.discountId() + " not found"));
            
            productDB.setDiscount(discountDB);
        }

        List<Category> categories = new ArrayList<>();
        for (Long id : requestProductDto.categoryIds()) {
            Category categoryDB = categoryRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + id + " not found"));
            
            categories.add(categoryDB);
        }
        
        productDB.setCategories(categories);

        productRepository.save(ProductDtoMapper.mapRequestProductDtoToProduct(requestProductDto));
        return ProductDtoMapper.mapProductToResponseProductDto(productDB);
    }

    @Override
    public ResponseProductDto getProductById(Long id) throws ObjectNotFoundException {
        Product productDB = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + id + " not found"));

        return ProductDtoMapper.mapProductToResponseProductDto(productDB);
    }

    @Override
    public List<ResponseProductDto> getAllProducts() throws ObjectNotFoundException {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) throw new ObjectNotFoundException("No products found");

        return ProductDtoMapper.mapProductListToProductDtoList(products);
    }

    @Override
    public ResponseProductDto updateProductById(RequestProductDto requestProductDto, Long id) throws ObjectNotFoundException {
        Product productDB = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + id + " not found"));

        if (!requestProductDto.name().equals(productDB.getName()))
            productDB.setName(requestProductDto.name());
        if (!requestProductDto.description().equals(productDB.getDescription()))
            productDB.setDescription(requestProductDto.description());
        if (requestProductDto.amount() != productDB.getAmount())
            productDB.setAmount(requestProductDto.amount());
        if (requestProductDto.price() != productDB.getPrice())
            productDB.setPrice(requestProductDto.price());
        if (requestProductDto.discountId() == null) {
            productDB.setDiscount(null);
        } else {
            Discount discountDB = discountRepository.findById(requestProductDto.discountId())
                    .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + requestProductDto.discountId() + " not found"));

            productDB.setDiscount(discountDB);
        }
        List<Category> categories = new ArrayList<>();
        for (Long categoryId : requestProductDto.categoryIds()) {
            Category categoryDB = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + categoryId + " not found"));

            categories.add(categoryDB);
        }

        productDB.setCategories(categories);

        productRepository.save(productDB);
        return ProductDtoMapper.mapProductToResponseProductDto(productDB);
    }

    @Override
    public void deleteProductById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = productRepository.deleteProductByProductId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Product with id = " + id + " not found");
    }
}
