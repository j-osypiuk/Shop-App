package com.example.shopapp.order;

import com.example.shopapp.order.dto.OrderDto;
import com.example.shopapp.order.dto.PostOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public PostOrderDto saveOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable("id") Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/product/{id}")
    public List<OrderDto> getAllOrdersByProductId(@PathVariable("id") Long id) {
        return orderService.getAllOrdersByProductId(id);
    }

    @GetMapping("/customer/{id}")
    public List<OrderDto> getAllOrdersByCustomerId(@PathVariable("id") Long id) {
        return orderService.getAllOrdersByCustomerId(id);
    }

    @GetMapping(params = {"from","to"})
    public List<OrderDto> getAllOrdersByTimePeriod(@RequestParam("from") LocalDateTime fromTime,
                                                   @RequestParam("to") LocalDateTime toTime) {
        return orderService.getAllOrdersByTimePeriod(fromTime, toTime);
    }

    @GetMapping(params = "completed")
    public List<OrderDto> getAllOrdersByCustomerId(@RequestParam("completed") boolean isCompleted) {
        return orderService.getAllOrdersByCompletionStatus(isCompleted);
    }

    @PutMapping("/{id}")
    public OrderDto completeOrderById(@PathVariable Long id) {
        return orderService.completeOrderById(id);
    }

}
