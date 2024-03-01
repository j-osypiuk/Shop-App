package com.shopapp.order;


import com.shopapp.exception.InvalidStateException;
import com.shopapp.exception.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    Order saveOrder(Order order, Long userId) throws ObjectNotFoundException, InvalidStateException;
    Order getOrderById(Long id) throws ObjectNotFoundException;
    List<Order> getAllOrders() throws ObjectNotFoundException;
    List<Order> getAllOrdersByProductId(Long id) throws ObjectNotFoundException;
    List<Order> getAllOrdersByUserId(Long id) throws ObjectNotFoundException;
    List<Order> getAllOrdersByCompletionStatus(boolean isCompleted) throws ObjectNotFoundException;
    List<Order> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate) throws ObjectNotFoundException;
    Order completeOrderById(Long id) throws ObjectNotFoundException;
}
