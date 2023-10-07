package com.example.shopapp.discount;

import com.example.shopapp.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long discountId;
    @Column(
            nullable = false,
            length = 100
    )
    private String name;
    @Column(
            columnDefinition = "text"
    )
    private String description;
    @Column(
            nullable = false
    )
    private int discountPercent;
    @OneToMany(
            mappedBy = "discount"
    )
    private Set<Product> products;
}
