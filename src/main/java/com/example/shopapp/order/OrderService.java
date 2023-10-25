package com.example.shopapp.order;

import com.example.shopapp.order.dto.RequestOrderDto;
import com.example.shopapp.order.dto.ResponseOrderDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    ResponseOrderDto saveOrder(RequestOrderDto requestOrderDto);
    ResponseOrderDto getOrderById(Long id);
    List<ResponseOrderDto> getAllOrders();
    List<ResponseOrderDto> getAllOrdersByProductId(Long id);
    List<ResponseOrderDto> getAllOrdersByCustomerId(Long id);
    List<ResponseOrderDto> getAllOrdersByCompletionStatus(boolean isCompleted);
    List<ResponseOrderDto> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate);
    ResponseOrderDto completeOrderById(Long id);
}
