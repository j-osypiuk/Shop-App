package com.example.shopapp.order.dto;

import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.order.Order;
import com.example.shopapp.orderproduct.dto.OrderProductDtoMapper;
import com.example.shopapp.user.dto.UserDtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoMapper {

    public static ResponseOrderDto mapOrderToResponseOrderDto(Order order) {
        return new ResponseOrderDto(
                order.getOrderId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getTotalDiscount(),
                order.isCompleted(),
                UserDtoMapper.mapUserToOrderUserDto(order.getUser()),
                AddressDtoMapper.mapAddressToResponseAddressDto(order.getAddress()),
                order.getOrderProducts().stream()
                        .map(OrderProductDtoMapper::mapOrderProductToOrderProductDto)
                        .collect(Collectors.toList())
        );
    }

    public static Order mapRequestOrderDtoToOrder(RequestOrderDto requestOrderDto){
        return Order.builder()
                .orderDate(LocalDateTime.now())
                .isCompleted(requestOrderDto.isCompleted())
                .address(AddressDtoMapper.mapRequestAddressDtoToAddress(requestOrderDto.address()))
                .orderProducts(requestOrderDto.orderProducts().stream()
                        .map(OrderProductDtoMapper::mapOrderProductDtoToOrderProduct)
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<ResponseOrderDto> mapOrderListToResponseOrderDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderDtoMapper::mapOrderToResponseOrderDto)
                .collect(Collectors.toList());
    }
}
