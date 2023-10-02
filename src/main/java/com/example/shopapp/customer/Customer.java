package com.example.shopapp.customer;

import com.example.shopapp.address.Address;
import com.example.shopapp.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long customerId;
    @Column(
            nullable = false,
            length = 50
    )
    private String firstName;
    @Column(
            nullable = false,
            length = 50
    )
    private String lastName;
    @Column(
            nullable = false,
            unique = true,
            length = 150
    )
    private String email;
    @Column(
            nullable = false
    )
    private int age;
    @Column(
            nullable = false,
            length = 7
    )
    @Enumerated(
            EnumType.STRING
    )
    private Gender gender;
    @Column(
            unique = true,
            length = 10
    )
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "address_id",
            referencedColumnName = "addressId"
    )
    private Address address;
    @OneToMany(
            mappedBy = "customer"
    )
    private Set<Order> orders;
}
