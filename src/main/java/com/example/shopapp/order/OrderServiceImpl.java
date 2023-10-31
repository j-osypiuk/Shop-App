package com.example.shopapp.order;

import com.example.shopapp.address.Address;
import com.example.shopapp.address.AddressRepository;
import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.user.User;
import com.example.shopapp.user.UserRepository;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.order.dto.OrderDtoMapper;
import com.example.shopapp.order.dto.RequestOrderDto;
import com.example.shopapp.order.dto.ResponseOrderDto;
import com.example.shopapp.product.Product;
import com.example.shopapp.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseOrderDto saveOrder(RequestOrderDto requestOrderDto) throws ObjectNotFoundException {
        Optional<User> userDB = userRepository.findById(requestOrderDto.userId());

        Optional<Address> addressDB = addressRepository.findByCityAndStreetAndNumberAndPostalCode(
                requestOrderDto.address().city(),
                requestOrderDto.address().street(),
                requestOrderDto.address().number(),
                requestOrderDto.address().postalCode()
        );

        Order orderDB = OrderDtoMapper.mapRequestOrderDtoToOrder(requestOrderDto);

        if (userDB.isEmpty())
            throw new ObjectNotFoundException("User with id = " + requestOrderDto.userId() + " not found");
        else
            orderDB.setUser(userDB.get());

        if (addressDB.isPresent())
            orderDB.setAddress(addressDB.get());
        else
            orderDB.setAddress(addressRepository.save(AddressDtoMapper.mapRequestAddressDtoToAddress(requestOrderDto.address())));


        List<Product> products = new ArrayList<>();
        double price = 0;
        double totalDiscount = 0;
        for (Long productId : requestOrderDto.productIds()) {
            Optional<Product> productDB = productRepository.findById(productId);
            if (productDB.isEmpty()) {
                throw new ObjectNotFoundException("Product with id = " + productId + " not found");
            }
            else {
                productDB.get().setAmount(productDB.get().getAmount() - 1);
                price += productDB.get().getPrice();
                totalDiscount += productDB.get().getPrice() * productDB.get().getDiscount().getDiscountPercent() / 100;
                products.add(productDB.get());
            }
        }

        orderDB.setProducts(products);
        orderDB.setTotalPrice(price - totalDiscount);
        orderDB.setTotalDiscount(totalDiscount);

        orderDB = orderRepository.save(orderDB);

        return OrderDtoMapper.mapOrderToResponseOrderDto(orderDB);
    }

    @Override
    public ResponseOrderDto getOrderById(Long id) throws ObjectNotFoundException {
        Optional<Order> orderDB = orderRepository.findById(id);

        if (orderDB.isEmpty()) throw new ObjectNotFoundException("Order with id = " + id + " not found");

        return OrderDtoMapper.mapOrderToResponseOrderDto(orderDB.get());
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
        Optional<Order> orderDB = orderRepository.findById(id);

        if (orderDB.isEmpty()) throw new ObjectNotFoundException("Order with id = " + id + " not found");

        orderDB.get().setCompleted(true);

        Order updatedOrder = orderRepository.save(orderDB.get());

        return OrderDtoMapper.mapOrderToResponseOrderDto(updatedOrder);
    }
}
