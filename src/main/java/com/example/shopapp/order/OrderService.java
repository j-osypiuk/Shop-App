package com.example.shopapp.order;

import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.order.dto.RequestOrderDto;
import com.example.shopapp.order.dto.ResponseOrderDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    ResponseOrderDto saveOrder(RequestOrderDto requestOrderDto) throws ObjectNotFoundException;
    ResponseOrderDto getOrderById(Long id) throws ObjectNotFoundException;
    List<ResponseOrderDto> getAllOrders() throws ObjectNotFoundException;
    List<ResponseOrderDto> getAllOrdersByProductId(Long id) throws ObjectNotFoundException;
    List<ResponseOrderDto> getAllOrdersByUserId(Long id) throws ObjectNotFoundException;
    List<ResponseOrderDto> getAllOrdersByCompletionStatus(boolean isCompleted) throws ObjectNotFoundException;
    List<ResponseOrderDto> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate) throws ObjectNotFoundException;
    ResponseOrderDto completeOrderById(Long id) throws ObjectNotFoundException;
}
