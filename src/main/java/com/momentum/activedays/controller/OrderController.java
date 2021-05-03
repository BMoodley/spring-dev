package com.momentum.activedays.controller;

import com.momentum.activedays.dto.OrderDTO;
import com.momentum.activedays.entity.Order;
import com.momentum.activedays.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RepositoryRestController
@RequestMapping("/order/")
@RequiredArgsConstructor
@Api(description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Orders.")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @ApiOperation("Returns list of all Orders in the system.")
    @GetMapping(path = "/orders")
    public ResponseEntity<?> getOrders() {
        List<Order> orderList = orderService.getOrders();
        return ResponseEntity.ok(orderList);
    }

    @ApiOperation("Returns a specific order by order id. 404 if does not exist.")
    @GetMapping(path = "/order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") String id) {
        try {
            LOGGER.info("OrderController::: " + id);
            Order result = orderService.getOrder(id);

            return ResponseEntity.ok(result);
        }catch (RuntimeException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Resource Not Found", exc);
        }
    }

    @ApiOperation("Returns a specific order by customer id. 404 if does not exist.")
    @GetMapping(path = "/customer/{id}")
    public ResponseEntity<?> getCustomerOrder(@PathVariable("id") String id) {
        try {
            LOGGER.info("OrderController::: " + id);
            Order result = orderService.getCustomerOrder(id);

            return ResponseEntity.ok(result);
        }catch (RuntimeException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Resource Not Found", exc);
        }
    }

    @ApiOperation("Creates a new order if the customers points are enough for all products in the list. 424 if validation fails")
    @PostMapping(path = "/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        LOGGER.info("OrderController: " + orderDTO);
        Order result = orderService.purchaseOrder(orderDTO);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("{message: Customer does not have enough points}");
        }

        return ResponseEntity.ok(result);
    }

    @ApiOperation("Updates and existing order.")
    @PutMapping(path = "/order")
    public ResponseEntity<?> updateOrder(@RequestBody OrderDTO orderDTO) {
        LOGGER.info("OrderController: " + orderDTO);
        Order order = orderService.updateOrder(orderDTO);

        return ResponseEntity.ok(order);
    }

    @ApiOperation("Deletes a order. 404 if the order's identifier is not found.")
    @DeleteMapping(path = "/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") String id) {
        LOGGER.info("OrderController: " + id);
        String result = orderService.deleteOrder(id);

        return ResponseEntity.ok(result);
    }
}
