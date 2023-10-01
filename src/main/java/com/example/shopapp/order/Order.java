package com.example.shopapp.order;

import com.example.shopapp.address.Address;
import com.example.shopapp.customer.Customer;
import com.example.shopapp.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

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
    private Date orderDate;
    @Column(
            nullable = false
    )
    private double totalPrice;
    @Column(
            name = "completed",
            nullable = false
    )
    private boolean isCompleted;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "customer_id",
            referencedColumnName = "customerId"
    )
    private Customer customer;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "address_id",
            referencedColumnName = "addressId"
    )
    private Address address;
    @ManyToMany(mappedBy = "orders")
    private Set<Product> products;
}
