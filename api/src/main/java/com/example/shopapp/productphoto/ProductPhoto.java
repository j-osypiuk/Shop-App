package com.example.shopapp.productphoto;

import com.example.shopapp.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPhoto {

    @Id
    private UUID photoName;
    @Column(
            nullable = false
    )
    private int number;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "product_id",
            referencedColumnName = "productId"
    )
    private Product product;
}
