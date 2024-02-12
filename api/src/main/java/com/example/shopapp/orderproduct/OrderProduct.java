package com.example.shopapp.orderproduct;

import com.example.shopapp.order.Order;
import com.example.shopapp.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProduct {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long orderProductId;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "product_id",
            referencedColumnName = "productId"
    )
    private Product product;
    @ManyToOne
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "orderId"
    )
    private Order order;
    @Column(
            nullable = false
    )
    private int amount;
}
