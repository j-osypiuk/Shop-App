package com.example.order.dto;

import com.example.address.dto.AddressDtoMapper;
import com.example.order.Order;
import com.example.orderproduct.dto.OrderProductDtoMapper;
import com.example.user.dto.UserDtoMapper;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDtoMapper {

    public static ResponseOrderDto mapOrderToResponseOrderDto(Order order) {
        return ResponseOrderDto.builder()
                .id(order.getOrderId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .totalDiscount(order.getTotalDiscount())
                .isCompleted(order.isCompleted())
                .user(UserDtoMapper.mapUserToOrderUserDto(order.getUser()))
                .address(AddressDtoMapper.mapAddressToResponseAddressDto(order.getAddress()))
                .products(order.getOrderProducts().stream()
                            .map(OrderProductDtoMapper::mapOrderProductToOrderProductDto)
                            .toList())
                .build();
    }

    public static Order mapRequestOrderDtoToOrder(RequestOrderDto requestOrderDto){
        return Order.builder()
                .orderDate(LocalDateTime.now())
                .isCompleted(requestOrderDto.isCompleted())
                .address(AddressDtoMapper.mapRequestAddressDtoToAddress(requestOrderDto.address()))
                .orderProducts(requestOrderDto.orderProducts().stream()
                        .map(OrderProductDtoMapper::mapOrderProductDtoToOrderProduct)
                        .toList())
                .build();
    }

    public static List<ResponseOrderDto> mapOrderListToResponseOrderDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderDtoMapper::mapOrderToResponseOrderDto)
                .toList();
    }
}
