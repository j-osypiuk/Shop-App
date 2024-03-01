package com.shopapp.product;

import com.shopapp.category.Category;
import com.shopapp.discount.Discount;
import com.shopapp.orderproduct.OrderProduct;
import com.shopapp.productphoto.ProductPhoto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@EqualsAndHashCode
@Getter
@Setter
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
//    @JsonIgnore
//    @ManyToMany(mappedBy = "products")
//    private List<Order> orders;
    @OneToMany(
            mappedBy = "product"
    )
    private List<OrderProduct> orderProducts;
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
