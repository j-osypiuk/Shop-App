package com.example.shopapp.product;

import com.example.shopapp.category.Category;
import com.example.shopapp.category.CategoryRepository;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.discount.DiscountRepository;
import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.ObjectNotFoundException;
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
    public Product saveProduct(Product product) throws ObjectNotFoundException, DuplicateUniqueValueException {
        if (productRepository.existsProductByName(product.getName()))
            throw new DuplicateUniqueValueException("Product with name = " + product.getName() + " already exists");

        if (product.getDiscount() != null) {
            Discount discountDB = discountRepository.findById(product.getDiscount().getDiscountId())
                    .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + product.getDiscount().getDiscountId() + " not found"));
            product.setDiscount(discountDB);
        }

        List<Category> categories = new ArrayList<>();
        for (Category category : product.getCategories()) {
            Category categoryDB = categoryRepository.findById(category.getCategoryId())
                    .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + category.getCategoryId() + " not found"));
            
            categories.add(categoryDB);
        }
        product.setCategories(categories);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) throws ObjectNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + id + " not found"));
    }

    @Override
    public List<Product> getAllProducts() throws ObjectNotFoundException {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) throw new ObjectNotFoundException("No products found");

        return products;
    }

    @Override
    public List<Product> getProductsByCategory(Long id) throws ObjectNotFoundException {
        List<Product> products = productRepository.findAllByCategoryId(id);

        if (products.isEmpty()) throw new ObjectNotFoundException("No products found");

        return products;
    }

    @Override
    public Product updateProductById(Long id, Product product) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Product productDB = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + id + " not found"));

        if (productRepository.existsProductByName(product.getName()))
            throw new DuplicateUniqueValueException("Product with name = " + product.getName() + " already exists");

        if (!product.getName().equals(productDB.getName()))
            productDB.setName(product.getName());
        if (!product.getDescription().equals(productDB.getDescription()))
            productDB.setDescription(product.getDescription());
        if (product.getAmount() != productDB.getAmount())
            productDB.setAmount(product.getAmount());
        if (product.getPrice() != productDB.getPrice())
            productDB.setPrice(product.getPrice());
        if (product.getDiscount() == null) {
            productDB.setDiscount(null);
        } else {
            Discount discountDB = discountRepository.findById(product.getDiscount().getDiscountId())
                    .orElseThrow(() -> new ObjectNotFoundException("Discount with id = " + product.getDiscount().getDiscountId() + " not found"));

            productDB.setDiscount(discountDB);
        }
        List<Category> categories = new ArrayList<>();
        for (Category category : product.getCategories()) {
            Category categoryDB = categoryRepository.findById(category.getCategoryId())
                    .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + category.getCategoryId() + " not found"));

            categories.add(categoryDB);
        }

        productDB.setCategories(categories);

        return productRepository.save(productDB);
    }

    @Override
    public void deleteProductById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = productRepository.deleteProductById(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Product with id = " + id + " not found");
    }
}
