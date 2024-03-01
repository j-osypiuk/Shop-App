package com.shopapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailDetails {

    private String firstName;
    private String lastName;
    private List<OrderProductDetails> products;
    private OrderAddressDetails address;
    private String totalDiscount;
    private String totalPrice;
}
