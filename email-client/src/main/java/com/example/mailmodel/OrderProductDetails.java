package com.example.mailmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProductDetails {

    private String productName;
    private String amount;
    private String unitPrice;
    private String totalDiscount;
    private String totalPrice;
}
