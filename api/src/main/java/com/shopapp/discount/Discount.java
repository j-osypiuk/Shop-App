package com.shopapp.discount;

import com.shopapp.product.Product;
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
public class Discount {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long discountId;
    @Column(
            nullable = false,
            unique = true,
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
    private List<Product> products;
}
