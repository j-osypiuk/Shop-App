package com.example.shopapp.orderproduct.dto;

import com.example.shopapp.orderproduct.OrderProduct;
import com.example.shopapp.product.Product;

public class OrderProductDtoMapper {

    public static OrderProduct mapOrderProductDtoToOrderProduct(OrderProductDto orderProductDto) {
        return OrderProduct.builder()
                .product(Product.builder().productId(orderProductDto.productId()).build())
                .amount(orderProductDto.amount())
                .build();
    }

    public static OrderProductDto mapOrderProductToOrderProductDto(OrderProduct orderProduct) {
        return new OrderProductDto(
                orderProduct.getProduct().getProductId(),
                orderProduct.getAmount()
        );
    }
}
