package com.shopapp.orderproduct.dto;

import com.shopapp.orderproduct.OrderProduct;
import com.shopapp.product.Product;

public class OrderProductDtoMapper {

    public static OrderProduct mapOrderProductDtoToOrderProduct(OrderProductDto orderProductDto) {
        return OrderProduct.builder()
                .product(Product.builder().productId(orderProductDto.productId()).build())
                .amount(orderProductDto.amount())
                .build();
    }

    public static OrderProductDto mapOrderProductToOrderProductDto(OrderProduct orderProduct) {
        return OrderProductDto.builder()
                .productId(orderProduct.getProduct().getProductId())
                .amount(orderProduct.getAmount())
                .build();
    }
}
