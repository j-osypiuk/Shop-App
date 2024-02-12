package com.example.shopapp.order;

import com.example.shopapp.address.Address;
import com.example.shopapp.address.AddressRepository;
import com.example.shopapp.category.Category;
import com.example.shopapp.discount.Discount;
import com.example.shopapp.exception.InvalidStateException;
import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.orderproduct.OrderProduct;
import com.example.shopapp.orderproduct.OrderProductRepository;
import com.example.shopapp.product.Product;
import com.example.shopapp.product.ProductRepository;
import com.example.shopapp.user.User;
import com.example.shopapp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(
                orderRepository,
                productRepository,
                addressRepository,
                userRepository,
                orderProductRepository
        );
    }

    @Test
    void saveOrderWithExistingAddressSavesOrder() throws ObjectNotFoundException, InvalidStateException {
        // given
        Long userId = 1L;
        int product1Amount = 20;
        int product2Amount = 30;
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Discount discount = Discount.builder()
                .name("Winter discount")
                .discountPercent(15)
                .build();
        Product foundProduct1 = Product.builder()
                .name("Orange juice")
                .price(100)
                .amount(product1Amount)
                .description("Orange juice description")
                .categories(Arrays.asList(category))
                .discount(discount)
                .build();
        Product foundProduct2 = Product.builder()
                .name("Melons")
                .price(200)
                .amount(product2Amount)
                .description("Melons description")
                .categories(Arrays.asList(category))
                .build();
        Address foundAddress = Address.builder()
                .country("England")
                .region("Sth London")
                .city("London")
                .street("Johnson Avenue")
                .number("24")
                .postalCode("32423")
                .build();
        User user = User.builder()
                .userId(userId)
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(foundProduct1)
                .amount(10)
                .build();
        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(foundProduct2)
                .amount(10)
                .build();

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderProducts(Arrays.asList(orderProduct1, orderProduct2))
                .address(foundAddress)
                .isCompleted(false)
                .user(user)
                .build();
        double totalDiscount = 0;
        double totalPrice = 0;
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            totalPrice += orderProduct.getProduct().getPrice() * orderProduct.getAmount();
            if (orderProduct.getProduct().getDiscount() != null)
                totalDiscount += orderProduct.getProduct().getPrice() * orderProduct.getAmount() * orderProduct.getProduct().getDiscount().getDiscountPercent() / 100;
        }
        totalPrice = totalPrice - totalDiscount;

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(addressRepository.findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getNumber(),
                order.getAddress().getPostalCode()
        )).willReturn(Optional.of(foundAddress));
        given(productRepository.findById(foundProduct1.getProductId())).willReturn(Optional.of(foundProduct1), Optional.of(foundProduct2));
        given(orderRepository.save(order)).willReturn(order);
        // when
        Order savedOrder = orderService.saveOrder(order, userId);
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> addressCityCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressStreetCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressPostalCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> product1IdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> product2IdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(userRepository).findById(userIdCaptor.capture());
        verify(addressRepository).findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                addressCityCaptor.capture(),
                addressStreetCaptor.capture(),
                addressNumberCaptor.capture(),
                addressPostalCodeCaptor.capture()
        );
        verify(addressRepository, never()).save(any());
        verify(productRepository, times(2)).findById(product1IdCaptor.capture());
        verify(productRepository, times(2)).findById(product2IdCaptor.capture());
        verify(orderRepository).save(orderCaptor.capture());
        verify(orderProductRepository, times(order.getOrderProducts().size())).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(addressCityCaptor.getValue()).isEqualTo(foundAddress.getCity());
        assertThat(addressStreetCaptor.getValue()).isEqualTo(foundAddress.getStreet());
        assertThat(addressNumberCaptor.getValue()).isEqualTo(foundAddress.getNumber());
        assertThat(addressPostalCodeCaptor.getValue()).isEqualTo(foundAddress.getPostalCode());
        assertThat(product1IdCaptor.getValue()).isEqualTo(foundProduct1.getProductId());
        assertThat(product2IdCaptor.getValue()).isEqualTo(foundProduct2.getProductId());
        assertThat(orderCaptor.getValue()).isEqualTo(order);
        assertEquals(savedOrder, order);
        assertEquals(savedOrder.getTotalPrice(), totalPrice);
        assertEquals(savedOrder.getTotalDiscount(), totalDiscount);
        assertFalse(savedOrder.isCompleted());
    }

    @Test
    void saveOrderWithNonExistentAddressSavesOrderAndItsAddress() throws ObjectNotFoundException, InvalidStateException {
        // given
        Long userId = 1L;
        int product1Amount = 10;
        int product2Amount = 20;
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Discount discount = Discount.builder()
                .name("Winter discount")
                .discountPercent(15)
                .build();
        Product foundProduct1 = Product.builder()
                .name("Orange juice")
                .price(100)
                .amount(product1Amount)
                .description("Orange juice description")
                .categories(Arrays.asList(category))
                .discount(discount)
                .build();
        Product foundProduct2 = Product.builder()
                .name("Melons")
                .price(200)
                .amount(product2Amount)
                .description("Melons description")
                .categories(Arrays.asList(category))
                .build();
        Address address = Address.builder()
                .country("England")
                .region("Sth London")
                .city("London")
                .street("Johnson Avenue")
                .number("24")
                .postalCode("32423")
                .build();
        User user = User.builder()
                .userId(userId)
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();

        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(foundProduct1)
                .amount(10)
                .build();
        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(foundProduct2)
                .amount(10)
                .build();

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderProducts(Arrays.asList(orderProduct1, orderProduct2))
                .address(address)
                .isCompleted(false)
                .user(user)
                .build();
        double totalDiscount = 0;
        double totalPrice = 0;
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            totalPrice += orderProduct.getProduct().getPrice() * orderProduct.getAmount();
            if (orderProduct.getProduct().getDiscount() != null)
                totalDiscount += orderProduct.getProduct().getPrice() * orderProduct.getAmount() * orderProduct.getProduct().getDiscount().getDiscountPercent() / 100;
        }
        totalPrice = totalPrice - totalDiscount;

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(addressRepository.findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getNumber(),
                order.getAddress().getPostalCode()
        )).willReturn(Optional.empty());
        given(addressRepository.save(order.getAddress())).willReturn(order.getAddress());
        given(productRepository.findById(foundProduct1.getProductId())).willReturn(Optional.of(foundProduct1), Optional.of(foundProduct2));
        given(orderRepository.save(order)).willReturn(order);
        // when
        Order savedOrder = orderService.saveOrder(order, userId);
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> addressCityCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressStreetCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressPostalCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
        ArgumentCaptor<Long> product1IdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> product2IdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(userRepository).findById(userIdCaptor.capture());
        verify(addressRepository).findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                addressCityCaptor.capture(),
                addressStreetCaptor.capture(),
                addressNumberCaptor.capture(),
                addressPostalCodeCaptor.capture()
        );
        verify(addressRepository).save(addressCaptor.capture());
        verify(productRepository, times(2)).findById(product1IdCaptor.capture());
        verify(productRepository, times(2)).findById(product2IdCaptor.capture());
        verify(orderRepository).save(orderCaptor.capture());
        verify(orderProductRepository, times(order.getOrderProducts().size())).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(addressCityCaptor.getValue()).isEqualTo(address.getCity());
        assertThat(addressStreetCaptor.getValue()).isEqualTo(address.getStreet());
        assertThat(addressNumberCaptor.getValue()).isEqualTo(address.getNumber());
        assertThat(addressPostalCodeCaptor.getValue()).isEqualTo(address.getPostalCode());
        assertThat(addressCaptor.getValue()).isEqualTo(address);
        assertThat(product1IdCaptor.getValue()).isEqualTo(foundProduct1.getProductId());
        assertThat(product2IdCaptor.getValue()).isEqualTo(foundProduct2.getProductId());
        assertThat(orderCaptor.getValue()).isEqualTo(order);
        assertEquals(savedOrder, order);
        assertEquals(savedOrder.getTotalPrice(), totalPrice);
        assertEquals(savedOrder.getTotalDiscount(), totalDiscount);
        assertFalse(savedOrder.isCompleted());
    }

    @Test
    void saveOrderThrowsExceptionIfUserWithGivenIdDoesNotExists() {
        // given
        Long userId = 1L;
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .isCompleted(false)
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> orderService.saveOrder(order, userId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User with id = " + userId + " not found");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(addressRepository, never()).findDistinctAddressByCityAndStreetAndNumberAndPostalCode(any(), any(), any(), any());
        verify(addressRepository, never()).save(any());
        verify(productRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
        verify(orderProductRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void saveOrderThrowsExceptionIfGivenProductDoesNotExists() {
        // given
        Long userId = 1L;
        int product1Amount = 10;
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Discount discount = Discount.builder()
                .name("Winter discount")
                .discountPercent(15)
                .build();
        Product foundProduct1 = Product.builder()
                .name("Orange juice")
                .price(100)
                .amount(product1Amount)
                .description("Orange juice description")
                .categories(Arrays.asList(category))
                .discount(discount)
                .build();
        Address foundAddress = Address.builder()
                .country("England")
                .region("Sth London")
                .city("London")
                .street("Johnson Avenue")
                .number("24")
                .postalCode("32423")
                .build();
        User user = User.builder()
                .userId(userId)
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(foundProduct1)
                .amount(10)
                .build();

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderProducts(Arrays.asList(orderProduct1))
                .address(foundAddress)
                .isCompleted(false)
                .user(user)
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(addressRepository.findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getNumber(),
                order.getAddress().getPostalCode()
        )).willReturn(Optional.of(foundAddress));
        given(productRepository.findById(foundProduct1.getProductId())).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> addressCityCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressStreetCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressPostalCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> product1IdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> orderService.saveOrder(order, userId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Product with id = " + foundProduct1.getProductId() + " not found");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(addressRepository).findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                addressCityCaptor.capture(),
                addressStreetCaptor.capture(),
                addressNumberCaptor.capture(),
                addressPostalCodeCaptor.capture()
        );
        verify(addressRepository, never()).save(any());
        verify(productRepository).findById(product1IdCaptor.capture());
        verify(orderRepository, never()).save(any());
        verify(orderProductRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(addressCityCaptor.getValue()).isEqualTo(foundAddress.getCity());
        assertThat(addressStreetCaptor.getValue()).isEqualTo(foundAddress.getStreet());
        assertThat(addressNumberCaptor.getValue()).isEqualTo(foundAddress.getNumber());
        assertThat(addressPostalCodeCaptor.getValue()).isEqualTo(foundAddress.getPostalCode());
        assertThat(product1IdCaptor.getValue()).isEqualTo(foundProduct1.getProductId());
    }

    @Test
    void saveOrderThrowsExceptionIfAmountOfProductInStockIsLowerThanAmountOfProductInOrder() throws ObjectNotFoundException, InvalidStateException {
        // given
        Long userId = 1L;
        int product1Amount = 5;
        Category category = Category.builder()
                .categoryId(1L)
                .build();
        Discount discount = Discount.builder()
                .name("Winter discount")
                .discountPercent(15)
                .build();
        Product foundProduct1 = Product.builder()
                .name("Orange juice")
                .price(100)
                .amount(product1Amount)
                .description("Orange juice description")
                .categories(Arrays.asList(category))
                .discount(discount)
                .build();
        Address foundAddress = Address.builder()
                .country("England")
                .region("Sth London")
                .city("London")
                .street("Johnson Avenue")
                .number("24")
                .postalCode("32423")
                .build();
        User user = User.builder()
                .userId(userId)
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(foundProduct1)
                .amount(10)
                .build();
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderProducts(Arrays.asList(orderProduct1))
                .address(foundAddress)
                .isCompleted(false)
                .user(user)
                .build();
        double totalDiscount = 0;
        double totalPrice = 0;
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            totalPrice += orderProduct.getProduct().getPrice() * orderProduct.getAmount();
            if (orderProduct.getProduct().getDiscount() != null)
                totalDiscount += orderProduct.getProduct().getPrice() * orderProduct.getAmount() * orderProduct.getProduct().getDiscount().getDiscountPercent() / 100;
        }
        totalPrice = totalPrice - totalDiscount;

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(addressRepository.findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                order.getAddress().getCity(),
                order.getAddress().getStreet(),
                order.getAddress().getNumber(),
                order.getAddress().getPostalCode()
        )).willReturn(Optional.of(foundAddress));
        given(productRepository.findById(foundProduct1.getProductId())).willReturn(Optional.of(foundProduct1));
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> addressCityCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressStreetCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressPostalCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> product1IdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> orderService.saveOrder(order, userId))
                .isInstanceOf(InvalidStateException.class)
                .hasMessageContaining("Not enough product with id = " + foundProduct1.getProductId() + " in stock");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(addressRepository).findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                addressCityCaptor.capture(),
                addressStreetCaptor.capture(),
                addressNumberCaptor.capture(),
                addressPostalCodeCaptor.capture()
        );
        verify(addressRepository, never()).save(any());
        verify(productRepository).findById(product1IdCaptor.capture());
        verify(orderRepository, never()).save(any());
        verify(orderProductRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(addressCityCaptor.getValue()).isEqualTo(foundAddress.getCity());
        assertThat(addressStreetCaptor.getValue()).isEqualTo(foundAddress.getStreet());
        assertThat(addressNumberCaptor.getValue()).isEqualTo(foundAddress.getNumber());
        assertThat(addressPostalCodeCaptor.getValue()).isEqualTo(foundAddress.getPostalCode());
        assertThat(product1IdCaptor.getValue()).isEqualTo(foundProduct1.getProductId());
    }
    @Test
    void getOrderByIdReturnsOrder() throws ObjectNotFoundException {
        // given
        Long orderId = 1L;
        Order order = Order.builder().orderId(orderId).build();
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        // when
        Order foundOrder = orderService.getOrderById(orderId);
        // then
        ArgumentCaptor<Long> orderIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(orderRepository).findById(orderIdCaptor.capture());
        assertThat(orderIdCaptor.getValue()).isEqualTo(orderId);
        assertEquals(foundOrder, order);
    }

    @Test
    void getOrderByIdThrowsExceptionIfOrderDoesNotExists() {
        // given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> orderIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> orderService.getOrderById(orderId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Order with id = " + orderId + " not found");
        verify(orderRepository).findById(orderIdCaptor.capture());
        assertThat(orderIdCaptor.getValue()).isEqualTo(orderId);
    }

    @Test
    void getAllOrdersReturnsListOfOrders() throws ObjectNotFoundException {
        // given
        Order order1 = Order.builder().orderId(1L).build();
        Order order2 = Order.builder().orderId(2L).build();
        given(orderRepository.findAll()).willReturn(Arrays.asList(order1, order2));
        // when
        List<Order> orders = orderService.getAllOrders();
        // then
        verify(orderRepository).findAll();
        assertEquals(orders.size(), 2);
        assertEquals(orders.get(0), order1);
        assertEquals(orders.get(1), order2);
    }

    @Test
    void getAllOrdersThrowsExceptionIfNoOrdersExists() {
        // given
        given(orderRepository.findAll()).willReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(() -> orderService.getAllOrders())
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No orders found");
        verify(orderRepository).findAll();
    }

    @Test
    void getAllOrdersByProductIdReturnsListOfOrders() throws ObjectNotFoundException {
        // given
        Long productId = 1L;
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(Product.builder().productId(1L).build())
                .amount(10)
                .build();
        Order order1 = Order.builder().orderId(1L).orderProducts(Arrays.asList(orderProduct1)).build();
        Order order2 = Order.builder().orderId(1L).orderProducts(Arrays.asList(orderProduct1)).build();
        given(orderRepository.findAllByProductId(productId)).willReturn(Arrays.asList(order1, order2));
        // when
        List<Order> orders = orderService.getAllOrdersByProductId(productId);
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(orderRepository).findAllByProductId(productIdCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
        assertEquals(orders.size(), 2);
        assertEquals(orders.get(0), order1);
        assertEquals(orders.get(1), order2);
    }

    @Test
    void getAllOrdersByProductIdThrowsExceptionIfNoOrdersExists() {
        // given
        Long productId = 1L;
        given(orderRepository.findAllByProductId(productId)).willReturn(Collections.emptyList());
        // when
        // then
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> orderService.getAllOrdersByProductId(productId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No orders for product with id = " + productId + " found");
        verify(orderRepository).findAllByProductId(productIdCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
    }

    @Test
    void getAllOrdersByUserIdReturnsListOfOrders() throws ObjectNotFoundException {
        // given
        Long userId = 1L;
        Order order1 = Order.builder().orderId(1L).user(User.builder().userId(userId).build()).build();
        Order order2 = Order.builder().orderId(1L).user(User.builder().userId(userId).build()).build();
        given(orderRepository.findAllByUserId(userId)).willReturn(Arrays.asList(order1, order2));
        // when
        List<Order> orders = orderService.getAllOrdersByUserId(userId);
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(orderRepository).findAllByUserId(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertEquals(orders.size(), 2);
        assertEquals(orders.get(0), order1);
        assertEquals(orders.get(1), order2);
    }

    @Test
    void getAllOrdersByUserIdThrowsExceptionIfNoOrdersExists() {
        // given
        Long userId = 1L;
        given(orderRepository.findAllByUserId(userId)).willReturn(Collections.emptyList());
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> orderService.getAllOrdersByUserId(userId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No orders for user with id = " + userId + " found");
        verify(orderRepository).findAllByUserId(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void getAllOrdersByCompletionStatusReturnsListOfOrders() throws ObjectNotFoundException {
        // given
        boolean completionStatus = true;
        Order order1 = Order.builder().orderId(1L).isCompleted(completionStatus).build();
        Order order2 = Order.builder().orderId(1L).isCompleted(completionStatus).build();
        given(orderRepository.findAllByCompletionStatus(completionStatus)).willReturn(Arrays.asList(order1, order2));
        // when
        List<Order> orders = orderService.getAllOrdersByCompletionStatus(completionStatus);
        // then
        ArgumentCaptor<Boolean> completionStatusCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(orderRepository).findAllByCompletionStatus(completionStatusCaptor.capture());
        assertThat(completionStatusCaptor.getValue()).isEqualTo(completionStatus);
        assertEquals(orders.size(), 2);
        assertEquals(orders.get(0), order1);
        assertEquals(orders.get(1), order2);
    }

    @Test
    void getAllOrdersByCompletionStatusThrowsExceptionIfNoOrdersExists() {
        // given
        boolean completionsStatus = true;
        given(orderRepository.findAllByCompletionStatus(completionsStatus)).willReturn(Collections.emptyList());
        // when
        // then
        ArgumentCaptor<Boolean> completionStatusCaptor = ArgumentCaptor.forClass(Boolean.class);
        assertThatThrownBy(() -> orderService.getAllOrdersByCompletionStatus(completionsStatus))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No orders with completion status = " + completionsStatus + " found");
        verify(orderRepository).findAllByCompletionStatus(completionStatusCaptor.capture());
        assertThat(completionStatusCaptor.getValue()).isEqualTo(completionsStatus);
    }

    @Test
    void getAllOrdersByTimePeriodReturnsListOfOrders() throws ObjectNotFoundException {
        // given
        LocalDateTime fromDate = LocalDateTime.of(2019,10,25,12,0);
        LocalDateTime toDate = LocalDateTime.of(2019,10,30,12,0);
        Order order1 = Order.builder().orderId(1L).orderDate(LocalDateTime.of(2019,10,26,12,0)).build();
        Order order2 = Order.builder().orderId(1L).orderDate(LocalDateTime.of(2019,10,28,12,0)).build();
        given(orderRepository.findAllByTimePeriod(fromDate, toDate)).willReturn(Arrays.asList(order1, order2));
        // when
        List<Order> orders = orderService.getAllOrdersByTimePeriod(fromDate, toDate);
        // then
        ArgumentCaptor<LocalDateTime> fromDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> toDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(orderRepository).findAllByTimePeriod(fromDateCaptor.capture(), toDateCaptor.capture());
        assertThat(fromDateCaptor.getValue()).isEqualTo(fromDate);
        assertThat(toDateCaptor.getValue()).isEqualTo(toDate);
        assertEquals(orders.size(), 2);
        assertEquals(orders.get(0), order1);
        assertEquals(orders.get(1), order2);
    }

    @Test
    void getAllOrdersByTimePeriodThrowsExceptionIfNoOrdersExists() {
        // given
        LocalDateTime fromDate = LocalDateTime.of(2019,10,25,12,0);
        LocalDateTime toDate = LocalDateTime.of(2019,10,30,12,0);
        given(orderRepository.findAllByTimePeriod(fromDate, toDate)).willReturn(Collections.emptyList());
        // when
        // then
        ArgumentCaptor<LocalDateTime> fromDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> toDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        assertThatThrownBy(() -> orderService.getAllOrdersByTimePeriod(fromDate, toDate))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No orders with given time period found");
        verify(orderRepository).findAllByTimePeriod(fromDateCaptor.capture(), toDateCaptor.capture());
        assertThat(fromDateCaptor.getValue()).isEqualTo(fromDate);
        assertThat(toDateCaptor.getValue()).isEqualTo(toDate);
    }

    @Test
    void completeOrderByIdMarksOrderAsCompleted() throws ObjectNotFoundException {
        // given
        Long orderId = 1L;
        Order order = Order.builder().orderId(orderId).isCompleted(false).build();
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(orderRepository.save(order)).willReturn(order);
        // when
        Order updatedOrder = orderService.completeOrderById(orderId);
        // then
        ArgumentCaptor<Long> orderIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).findById(orderIdCaptor.capture());
        verify(orderRepository).save(orderCaptor.capture());
        assertThat(orderIdCaptor.getValue()).isEqualTo(orderId);
        assertThat(orderCaptor.getValue()).isEqualTo(order);
        assertTrue(updatedOrder.isCompleted());
        assertEquals(updatedOrder, order);
    }

    @Test
    void completeOrderByIdThrowsExceptionIfOrderDoesNotExists() {
        // given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> orderIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> orderService.getOrderById(orderId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Order with id = " + orderId + " not found");
        verify(orderRepository).findById(orderIdCaptor.capture());
        verify(orderRepository, never()).save(any());
    }
}