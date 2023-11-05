package com.example.shopapp.order;

import com.example.shopapp.address.Address;
import com.example.shopapp.address.AddressRepository;
import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.order.dto.OrderDtoMapper;
import com.example.shopapp.order.dto.RequestOrderDto;
import com.example.shopapp.order.dto.ResponseOrderDto;
import com.example.shopapp.product.Product;
import com.example.shopapp.product.ProductRepository;
import com.example.shopapp.user.User;
import com.example.shopapp.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            AddressRepository addressRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseOrderDto saveOrder(RequestOrderDto requestOrderDto) throws ObjectNotFoundException {
        User userDB = userRepository.findById(requestOrderDto.userId())
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + requestOrderDto.userId() + " not found"));

        Address addressDB = addressRepository.findByCityAndStreetAndNumberAndPostalCode(
                requestOrderDto.address().city(),
                requestOrderDto.address().street(),
                requestOrderDto.address().number(),
                requestOrderDto.address().postalCode()
        ).orElse(addressRepository.save(AddressDtoMapper.mapRequestAddressDtoToAddress(requestOrderDto.address())));

        Order orderDB = OrderDtoMapper.mapRequestOrderDtoToOrder(requestOrderDto);
        orderDB.setUser(userDB);
        orderDB.setAddress(addressDB);

        List<Product> products = new ArrayList<>();
        double price = 0;
        double totalDiscount = 0;
        for (Long productId : requestOrderDto.productIds()) {
            Product productDB = productRepository.findById(productId)
                    .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + productId + " not found"));

            productDB.setAmount(productDB.getAmount() - 1);
            price += productDB.getPrice();
            totalDiscount += productDB.getPrice() * productDB.getDiscount().getDiscountPercent() / 100;
            products.add(productDB);
        }

        orderDB.setProducts(products);
        orderDB.setTotalPrice(price - totalDiscount);
        orderDB.setTotalDiscount(totalDiscount);

        orderRepository.save(orderDB);
        return OrderDtoMapper.mapOrderToResponseOrderDto(orderDB);
    }

    @Override
    public ResponseOrderDto getOrderById(Long id) throws ObjectNotFoundException {
        Order orderDB = orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order with id = " + id + " not found"));

        return OrderDtoMapper.mapOrderToResponseOrderDto(orderDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrders() throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAll();

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders found");

        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByProductId(Long id) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByProductsProductId(id);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders for product with id = " + id + " found");

        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByUserId(Long id) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByUserUserId(id);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders for user with id = " + id + " found");

        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByCompletionStatus(boolean isCompleted) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByIsCompleted(isCompleted);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders with completion status = " + isCompleted + " found");

        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public List<ResponseOrderDto> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByOrderDateBetween(fromDate, toDate);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders with given time period found");

        return OrderDtoMapper.mapOrderListToResponseOrderDtoList(ordersDB);
    }

    @Override
    public ResponseOrderDto completeOrderById(Long id) throws ObjectNotFoundException {
        Order orderDB = orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order with id = " + id + " not found"));

        orderDB.setCompleted(true);

        orderRepository.save(orderDB);
        return OrderDtoMapper.mapOrderToResponseOrderDto(orderDB);
    }
}
