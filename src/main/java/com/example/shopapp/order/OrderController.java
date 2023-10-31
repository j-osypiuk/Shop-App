package com.example.shopapp.order;

import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.order.dto.RequestOrderDto;
import com.example.shopapp.order.dto.ResponseOrderDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<ResponseOrderDto> saveOrder(@Valid @RequestBody RequestOrderDto order) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.saveOrder(order),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<ResponseOrderDto> getOrderById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.getOrderById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrders() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.getAllOrders(),
                HttpStatus.OK
        );
    }

    @GetMapping("/product/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByProductId(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.getAllOrdersByProductId(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByUserId(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.getAllOrdersByUserId(id),
                HttpStatus.OK
        );
    }

    @GetMapping(params = {"from", "to"})
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByTimePeriod(@RequestParam("from") LocalDateTime fromTime, @RequestParam("to") LocalDateTime toTime) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.getAllOrdersByTimePeriod(fromTime, toTime),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "completed")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ResponseOrderDto>> getAllOrdersByCompletionStatus(@RequestParam("completed") boolean isCompleted) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.getAllOrdersByCompletionStatus(isCompleted),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseOrderDto> completeOrderById(@PathVariable Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                orderService.completeOrderById(id),
                HttpStatus.OK
        );
    }
}
