package com.momentum.activedays.service;

import com.momentum.activedays.dto.OrderDTO;
import com.momentum.activedays.entity.Customer;
import com.momentum.activedays.entity.Order;
import com.momentum.activedays.repository.CustomerRepository;
import com.momentum.activedays.repository.OrderRepository;
import com.momentum.activedays.repository.ProductsRepository;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public OrderService() {}

    @Timed(value = "orders.time", description = "Time taken to return all orders from mongodb")
    public List<Order> getOrders() {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> orderRepository.findAll(),
                throwable -> getDefaultOrders());
    }

    /**
     * Fallback method when getOrders fails to fetch data from mongodb
     * We should usually implement caching like Redis,
     * but for this project we will use sample data to demonstrate
     * @return fallback list of orders
     */
    public List<Order> getDefaultOrders() {
//        Should implement caching mechanism as a failover approach
        Order order = new Order();
        order.setId("1234");
        order.setCustomer("608eaf8a0d375292bcf052e1");
        List<String> products = new ArrayList<>();
        products.add("608eaf280d375292bcf052e0");
        order.setProducts(products);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        return orders;
    }

    public Order getOrder(String id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.get();
    }

    public Order getCustomerOrder(String id) {
        Optional<Order> order = orderRepository.findByCustomer(id);
        return order.get();
    }

    /**
     * When a customer wants to purchase 1 or many orders
     * this method will validate if the customers points are enough to
     * purchase the product/s
     * @param orderDTO a customer id and a list of product ids
     * @return null if validation fails
     */
    public Order purchaseOrder(OrderDTO orderDTO) {
        String customerId = orderDTO.getCustomer();
        Integer customerPoints = customerService.getCustomerPoints(customerId);

        List<String> productIds = orderDTO.getProducts();
        Integer productTotalPoints = productService.getTotalPoints(productIds);
        LOGGER.info("OrderService: customer points : " + customerPoints + " ,products points: " + productTotalPoints);

        if (productTotalPoints > customerPoints) {
            return null;
        }
        else {
            return saveOrder(orderDTO);
        }
    }

    public Order saveOrder(OrderDTO orderDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Order order = modelMapper.map(orderDTO, Order.class);
        return orderRepository.save(order);
    }

    public Order updateOrder(OrderDTO orderDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Order order = modelMapper.map(orderDTO, Order.class);
        return orderRepository.save(order);
    }

    public String deleteOrder(String id) {
        orderRepository.deleteById(id);
        return id;
    }
}
