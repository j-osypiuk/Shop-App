package com.example.mailmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailModel {

    @Getter
    private String firstName;
    @Getter
    private String lastName;
    private List<OrderProductDetails> products;
    private OrderAddressDetails address;
    private String totalDiscount;
    private String totalPrice;
}
