package com.example.shopapp.order;

import com.example.shopapp.address.Address;
import com.example.shopapp.address.AddressRepository;
import com.example.shopapp.order.dto.OrderDto;
import com.example.shopapp.order.dto.OrderDtoMapper;
import com.example.shopapp.order.dto.PostOrderDto;
import com.example.shopapp.product.Product;
import com.example.shopapp.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public PostOrderDto saveOrder(Order order) {
        Optional<Address> addressDB = addressRepository.findById(order.getAddress().getAddressId());
        if (addressDB.isEmpty()) {
            addressRepository.save(order.getAddress());
        }

        for (Product product : order.getProducts()) {
            Optional<Product> productDB = productRepository.findById(product.getProductId());
            if (productDB.isPresent()) {
                productDB.get().setAmount(productDB.get().getAmount() - 1);
            }
        }

        order.setOrderDate(LocalDateTime.now());

        Order orderDB = orderRepository.save(order);

        return OrderDtoMapper.mapOrderToPostOrderDto(orderDB);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order orderDB = orderRepository.findById(id).get();
        return OrderDtoMapper.mapOrderToOrderDto(orderDB);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> ordersDB =  orderRepository.findAll();
        return OrderDtoMapper.mapOrderListToOrderDtoList(ordersDB);
    }

    @Override
    public List<OrderDto> getAllOrdersByProductId(Long id) {
        List<Order> ordersDB = orderRepository.findAllByProductsProductId(id);
        return OrderDtoMapper.mapOrderListToOrderDtoList(ordersDB);
    }

    @Override
    public List<OrderDto> getAllOrdersByCustomerId(Long id) {
        List<Order> ordersDB = orderRepository.findAllByCustomerCustomerId(id);
        return OrderDtoMapper.mapOrderListToOrderDtoList(ordersDB);
    }

    @Override
    public List<OrderDto> getAllOrdersByCompletionStatus(boolean isCompleted) {
        List<Order> ordersDB = orderRepository.findAllByIsCompleted(isCompleted);
        return OrderDtoMapper.mapOrderListToOrderDtoList(ordersDB);
    }

    @Override
    public List<OrderDto> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Order> ordersDB = orderRepository.findAllByOrderDateBetween(fromDate, toDate);
        return OrderDtoMapper.mapOrderListToOrderDtoList(ordersDB);
    }

    @Override
    public OrderDto completeOrderById(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setCompleted(true);

        Order updatedOrder = orderRepository.save(order);
        return OrderDtoMapper.mapOrderToOrderDto(updatedOrder);
    }
}
