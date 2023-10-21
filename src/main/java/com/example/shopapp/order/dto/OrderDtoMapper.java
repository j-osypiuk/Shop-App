package com.example.shopapp.order.dto;

import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.customer.dto.CustomerDtoMapper;
import com.example.shopapp.order.Order;
import com.example.shopapp.product.Product;
import com.example.shopapp.product.dto.ProductDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoMapper {

    public static OrderDto mapOrderToOrderDto(Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getTotalDiscount(),
                order.isCompleted(),
                CustomerDtoMapper.mapCustomerToOrderCustomerDto(order.getCustomer()),
                AddressDtoMapper.mapAddressToAddressDto(order.getAddress()),
                order.getProducts().stream()
                        .map(ProductDtoMapper::mapProductToOrderProductDto)
                        .collect(Collectors.toList())
        );
    }

    public static PostOrderDto mapOrderToPostOrderDto(Order order) {
        return new PostOrderDto(
                order.getOrderId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getTotalDiscount(),
                order.isCompleted(),
                order.getCustomer().getCustomerId(),
                order.getAddress().getAddressId(),
                order.getProducts().stream()
                        .map(Product::getProductId)
                        .collect(Collectors.toList())
        );
    }

    public static List<OrderDto> mapOrderListToOrderDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderDtoMapper::mapOrderToOrderDto)
                .collect(Collectors.toList());
    }
}
