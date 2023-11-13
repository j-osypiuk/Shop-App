package com.example.shopapp.order;

import com.example.shopapp.address.Address;
import com.example.shopapp.address.AddressRepository;
import com.example.shopapp.exception.ObjectNotFoundException;
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
    public Order saveOrder(Order order, Long userId) throws ObjectNotFoundException {
        User userDB = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + userId + " not found"));

        Address addressDB = addressRepository.findAddressByCityAndStreetAndNumberAndPostalCode(
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getNumber(),
                order.getAddress().getPostalCode()
        ).orElse(addressRepository.save(order.getAddress()));

        order.setUser(userDB);
        order.setAddress(addressDB);

        List<Product> products = new ArrayList<>();
        double price = 0;
        double totalDiscount = 0;
        for (Product product : order.getProducts()) {
            Product productDB = productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + product.getProductId() + " not found"));

            productDB.setAmount(productDB.getAmount() - 1);
            price += productDB.getPrice();
            totalDiscount += productDB.getPrice() * productDB.getDiscount().getDiscountPercent() / 100;
            products.add(productDB);
        }

        order.setProducts(products);
        order.setTotalPrice(price - totalDiscount);
        order.setTotalDiscount(totalDiscount);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) throws ObjectNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order with id = " + id + " not found"));
    }

    @Override
    public List<Order> getAllOrders() throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAll();

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders found");

        return ordersDB;
    }

    @Override
    public List<Order> getAllOrdersByProductId(Long id) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByProductId(id);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders for product with id = " + id + " found");

        return ordersDB;
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long id) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByUserId(id);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders for user with id = " + id + " found");

        return ordersDB;
    }

    @Override
    public List<Order> getAllOrdersByCompletionStatus(boolean isCompleted) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByCompletionStatus(isCompleted);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders with completion status = " + isCompleted + " found");

        return ordersDB;
    }

    @Override
    public List<Order> getAllOrdersByTimePeriod(LocalDateTime fromDate, LocalDateTime toDate) throws ObjectNotFoundException {
        List<Order> ordersDB = orderRepository.findAllByTimePeriod(fromDate, toDate);

        if (ordersDB.isEmpty()) throw new ObjectNotFoundException("No orders with given time period found");

        return ordersDB;
    }

    @Override
    public Order completeOrderById(Long id) throws ObjectNotFoundException {
        Order orderDB = orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order with id = " + id + " not found"));

        orderDB.setCompleted(true);

        return orderRepository.save(orderDB);
    }
}
