package com.shopapp.order;

import com.shopapp.address.Address;
import com.shopapp.address.AddressRepository;
import com.shopapp.emailservice.EmailService;
import com.shopapp.exception.InvalidStateException;
import com.shopapp.exception.ObjectNotFoundException;
import com.shopapp.mailmodel.MailModel;
import com.shopapp.mailmodel.OrderAddressDetails;
import com.shopapp.mailmodel.OrderProductDetails;
import com.shopapp.orderproduct.OrderProduct;
import com.shopapp.orderproduct.OrderProductRepository;
import com.shopapp.product.Product;
import com.shopapp.product.ProductRepository;
import com.shopapp.user.User;
import com.shopapp.user.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final EmailService emailService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            AddressRepository addressRepository,
                            UserRepository userRepository,
                            OrderProductRepository orderProductRepository,
                            @Lazy EmailService emailService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Order saveOrder(Order order, Long userId) throws ObjectNotFoundException, InvalidStateException {
        User userDB = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + userId + " not found"));

        order.setUser(userDB);

        Optional<Address> addressDB = addressRepository.findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getNumber(),
                order.getAddress().getPostalCode()
        );

        if (addressDB.isPresent())
            order.setAddress(addressDB.get());
        else
            addressRepository.save(order.getAddress());

        OrderAddressDetails mailModelAddress = new OrderAddressDetails(
                order.getAddress().getCountry(),
                order.getAddress().getRegion(),
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getNumber(),
                order.getAddress().getPostalCode()
        );

        double totalPrice = 0;
        double totalDiscount = 0;
        List<OrderProductDetails> orderProductsDetails = new ArrayList<>();

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product productDB = productRepository.findById(orderProduct.getProduct().getProductId())
                    .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + orderProduct.getProduct().getProductId() + " not found"));

            if (productDB.getAmount() - orderProduct.getAmount() < 0)
                throw new InvalidStateException("Not enough product with id = " + productDB.getProductId() + " in stock");

            productDB.setAmount(productDB.getAmount() - orderProduct.getAmount());

            double totalProductsPrice = productDB.getPrice() * orderProduct.getAmount();

            totalPrice += totalProductsPrice;

            double totalProductsDiscount = 0;

            if (productDB.getDiscount() != null) {
                totalProductsDiscount += productDB.getPrice() * orderProduct.getAmount() * productDB.getDiscount().getDiscountPercent() / 100;
                totalDiscount += totalProductsDiscount;
            }

            orderProductsDetails.add(new OrderProductDetails(
                    productDB.getName(),
                    String.valueOf(orderProduct.getAmount()),
                    "$" + productDB.getPrice(),
                    "$" + totalProductsDiscount,
                    "$" + (totalProductsPrice - totalProductsDiscount)
            ));
        }

        order.setTotalPrice(totalPrice - totalDiscount);
        order.setTotalDiscount(totalDiscount);

        Order orderDB = orderRepository.save(order);

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            orderProduct.setOrder(Order.builder().orderId(order.getOrderId()).build());
            orderProductRepository.save(orderProduct);
        }

        emailService.sendMail(orderDB.getUser().getEmail(), new MailModel(
                userDB.getFirstName(),
                userDB.getLastName(),
                orderProductsDetails,
                mailModelAddress,
                "$" + order.getTotalDiscount(),
                "$" + order.getTotalPrice()
        ));

        return orderDB;
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
