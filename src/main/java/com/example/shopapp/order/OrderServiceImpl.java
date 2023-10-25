package com.example.shopapp.order;

import com.example.shopapp.address.Address;
import com.example.shopapp.address.AddressRepository;
import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.order.dto.RequestOrderDto;
import com.example.shopapp.order.dto.ResponseOrderDto;
import com.example.shopapp.order.dto.OrderDtoMapper;
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
    public ResponseOrderDto saveOrder(RequestOrderDto requestOrderDto) {
        Optional<Address> addressDB = addressRepository.findByCityAndStreetAndNumberAndPostalCode(
                requestOrderDto.address().city(),
                requestOrderDto.address().street(),
                requestOrderDto.address().number(),
                requestOrderDto.address().postalCode()
        );

        Order orderDB = OrderDtoMapper.mapRequestOrderDtoToOrder(requestOrderDto);
        orderDB.setAddress(addressDB.get());
        Address newAddress;
        if (addressDB.isEmpty()) {
            newAddress = addressRepository.save(AddressDtoMapper.mapRequestAddressDtoToAddress(requestOrderDto.address()));
            orderDB.setAddress(newAddress);
        }

        for (Long productId : requestOrderDto.productIds()) {
            Optional<Product> productDB = productRepository.findById(productId);
            if (productDB.isPresent()) {
                productDB.get().setAmount(productDB.get().getAmount() - 1);
            }
        }

        orderDB = orderRepository.save(orderDB);

        return OrderDtoMapper.mapOrderToResponseOrderDto(orderDB);
    }

    @Override
    public ResponseOrderDto getOrderById(Long id) {
        Order orderDB = orderRepository.findById(id).get();
        return OrderDtoMapper.mapOrderToResponseOrderDto(orderDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrders() {
        List<Order> ordersDB =  orderRepository.findAll();
        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByProductId(Long id) {
        List<Order> ordersDB = orderRepository.findAllByProductsProductId(id);
        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByCustomerId(Long id) {
        List<Order> ordersDB = orderRepository.findAllByCustomerCustomerId(id);
        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByCompletionStatus(boolean isCompleted) {
        List<Order> ordersDB = orderRepository.findAllByIsCompleted(isCompleted);
        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Order> ordersDB = orderRepository.findAllByOrderDateBetween(fromDate, toDate);
        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public ResponseOrderDto completeOrderById(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setCompleted(true);

        Order updatedOrder = orderRepository.save(order);
        return OrderDtoMapper.mapOrderToResponseOrderDto(updatedOrder);
    }
}
