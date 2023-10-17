package com.example.shopapp.product;

import com.example.shopapp.category.Category;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.order.Order;
import com.example.shopapp.productphoto.ProductPhoto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long productId;
    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String name;
    @Column(
            columnDefinition = "text",
            nullable = false
    )
    private String description;
    @Column(
            nullable = false
    )
    private int amount;
    @Column(
            nullable = false
    )
    private double price;
    @ManyToOne
    @JoinColumn(
            name = "discount_id",
            referencedColumnName = "discountId"
    )
    private Discount discount;
    @ManyToMany
    @JoinTable(
            name = "product_order",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders;
    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "productId"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    )
    private List<Category> categories;
    @OneToMany(
            mappedBy = "product"
    )
    private List<ProductPhoto> productPhotos;
}
