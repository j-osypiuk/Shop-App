package com.example.shopapp.order;

import com.example.shopapp.order.dto.OrderDto;
import com.example.shopapp.order.dto.PostOrderDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    PostOrderDto saveOrder(Order order);
    OrderDto getOrderById(Long id);
    List<OrderDto> getAllOrders();
    List<OrderDto> getAllOrdersByProductId(Long id);
    List<OrderDto> getAllOrdersByCustomerId(Long id);
    List<OrderDto> getAllOrdersByCompletionStatus(boolean isCompleted);
    List<OrderDto> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate);
    OrderDto completeOrderById(Long id);
}
