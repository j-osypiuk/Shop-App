package com.example.shopapp.product;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.CategoryRepository;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.discount.DiscountRepository;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.product.dto.ProductDtoMapper;
import com.example.shopapp.product.dto.RequestProductDto;
import com.example.shopapp.product.dto.ResponseProductDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Product productDB = productRepository.save(ProductDtoMapper.mapRequestProductDtoToProduct(requestProductDto));

        if (requestProductDto.discountId() != null){
            Optional<Discount> discountDB = discountRepository.findById(requestProductDto.discountId());
            if (discountDB.isPresent())
                productDB.setDiscount(discountDB.get());
            else
                throw new ObjectNotFoundException("Discount with id = " + requestProductDto.discountId() + " not found");
        }

        List<Category> categories = new ArrayList<>();
        for (Long id : requestProductDto.categoryIds()) {
            Optional<Category> categoryDB = categoryRepository.findById(id);
            if (categoryDB.isPresent())
                categories.add(categoryDB.get());
            else
                throw new ObjectNotFoundException("Category with id = " + id + " not found");
        }

        productDB.setCategories(categories);

        return ProductDtoMapper.mapProductToResponseProductDto(productDB);
    }

    @Override
    public ResponseProductDto getProductById(Long id) throws ObjectNotFoundException {
        Optional<Product> productDB = productRepository.findById(id);

        if (productDB.isEmpty()) throw new ObjectNotFoundException("Product with id = " + id + " not found");

        return ProductDtoMapper.mapProductToResponseProductDto(productDB.get());
    }

    @Override
    public List<ResponseProductDto> getAllProducts() throws ObjectNotFoundException {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) throw new ObjectNotFoundException("No products found");

        return ProductDtoMapper.mapProductListToProductDtoList(products);
    }

    @Override
    public ResponseProductDto updateProductById(RequestProductDto requestProductDto, Long id) throws ObjectNotFoundException {
        Optional<Product> productDB = productRepository.findById(id);

        if (productDB.isEmpty()) throw new ObjectNotFoundException("Product with id = " + id + " not found");

        if (!requestProductDto.name().equals(productDB.get().getName())) productDB.get().setName(requestProductDto.name());
        if (!requestProductDto.description().equals(productDB.get().getDescription())) productDB.get().setDescription(requestProductDto.description());
        if (requestProductDto.amount() != productDB.get().getAmount()) productDB.get().setAmount(requestProductDto.amount());
        if (requestProductDto.price() != productDB.get().getPrice()) productDB.get().setPrice(requestProductDto.price());
        if (requestProductDto.discountId() == null) {
            productDB.get().setDiscount(null);
        } else {
            Optional<Discount> discountDB = discountRepository.findById(requestProductDto.discountId());
            if (discountDB.isEmpty()) throw new ObjectNotFoundException("Discount with id = " + requestProductDto.discountId() + " not found");
            else productDB.get().setDiscount(discountDB.get());
        }
        List<Category> categories = new ArrayList<>();
        for (Long categoryId : requestProductDto.categoryIds()) {
            Optional<Category> categoryDB = categoryRepository.findById(categoryId);
            if (categoryDB.isPresent())
                categories.add(categoryDB.get());
            else
                throw new ObjectNotFoundException("Category with id = " + categoryId + " not found");
        }

        productDB.get().setCategories(categories);

        Product updatedProduct = productRepository.save(productDB.get());
        return ProductDtoMapper.mapProductToResponseProductDto(updatedProduct);
    }

    @Override
    public void deleteProductById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = productRepository.deleteProductByProductId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Product with id = " + id + " not found");
    }
}
