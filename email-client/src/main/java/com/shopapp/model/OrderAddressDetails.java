package com.shopapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderAddressDetails {

    private String country;
    private String region;
    private String city;
    private String street;
    private String number;
    private String postalCode;
}
