package com.example.shopapp.address;

import com.example.shopapp.customer.Customer;
import com.example.shopapp.order.Order;
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
public class Address {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long addressId;
    @Column(
            nullable = false,
            length = 50)
    private String country;
    @Column(
            nullable = false,
            length = 50)
    private String region;
    @Column(
            nullable = false,
            length = 50)
    private String city;
    @Column(
            nullable = false,
            length = 50)
    private String street;
    @Column(
            nullable = false,
            length = 5)
    private String number;
    @Column(
            nullable = false,
            length = 10)
    private String postalCode;
    @OneToMany(
            mappedBy = "address"
    )
    private Set<Order> orders;
    @OneToOne(
            mappedBy = "address"
    )
    private Customer customer;
}
