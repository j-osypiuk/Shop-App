package com.shopapp.order;

import com.shopapp.address.Address;
import com.shopapp.orderproduct.OrderProduct;
import com.shopapp.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "order_details"
)
public class Order {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long orderId;
    @Column(
            nullable = false
    )
    private LocalDateTime orderDate;
    @Column(
            nullable = false
    )
    private double totalPrice;
    @Column(
            nullable = false
    )
    private double totalDiscount;
    @Column(
            name = "completed",
            nullable = false
    )
    private boolean isCompleted;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User user;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "address_id",
            referencedColumnName = "addressId"
    )
    private Address address;
//    @ManyToMany
//    @JoinTable(
//            name = "order_product",
//            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "orderId"),
//            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "productId", nullable = false)
//    )
//    private List<Product> products;
    @OneToMany(
            mappedBy = "order"
    )
    private List<OrderProduct> orderProducts;
}
