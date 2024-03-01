package com.shopapp.order;

import com.shopapp.exception.InvalidStateException;
import com.shopapp.exception.ObjectNotFoundException;
import com.shopapp.order.dto.OrderDtoMapper;
import com.shopapp.order.dto.RequestOrderDto;
import com.shopapp.order.dto.ResponseOrderDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@EnableTransactionManagement
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Long>> saveOrder(@Valid @RequestBody RequestOrderDto requestOrderDto, @PathVariable("id") Long userId) throws ObjectNotFoundException, InvalidStateException {
       Order order =  orderService.saveOrder(OrderDtoMapper.mapRequestOrderDtoToOrder(requestOrderDto), userId);

        return new ResponseEntity<>(
                Map.of("orderId", order.getOrderId()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDto> getOrderById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                OrderDtoMapper.mapOrderToResponseOrderDto(orderService.getOrderById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseOrderDto>> getAllOrders() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                OrderDtoMapper.mapOrderListToResponseOrderDtoList(orderService.getAllOrders()),
                HttpStatus.OK
        );
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByProductId(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                OrderDtoMapper.mapOrderListToResponseOrderDtoList(orderService.getAllOrdersByProductId(id)),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByUserId(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                OrderDtoMapper.mapOrderListToResponseOrderDtoList(orderService.getAllOrdersByUserId(id)),
                HttpStatus.OK
        );
    }

    @GetMapping(params = {"from", "to"})
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByTimePeriod(@RequestParam("from") LocalDateTime fromTime, @RequestParam("to") LocalDateTime toTime) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                OrderDtoMapper.mapOrderListToResponseOrderDtoList(orderService.getAllOrdersByTimePeriod(fromTime, toTime)),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "completed")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByCompletionStatus(@RequestParam("completed") boolean isCompleted) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                OrderDtoMapper.mapOrderListToResponseOrderDtoList(orderService.getAllOrdersByCompletionStatus(isCompleted)),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Long>> completeOrderById(@PathVariable Long id) throws ObjectNotFoundException {
        Order order = orderService.completeOrderById(id);

        return new ResponseEntity<>(
                Map.of("orderId", order.getOrderId()),
                HttpStatus.OK
        );
    }
}
