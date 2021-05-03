package com.momentum.activedays.controller;

import com.momentum.activedays.dto.CustomerDTO;
import com.momentum.activedays.entity.Customer;
import com.momentum.activedays.service.CustomerService;
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
@RequestMapping("/customer/")
@RequiredArgsConstructor
@Api(description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Customers.")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @ApiOperation("Returns list of all Customers in the system.")
    @GetMapping(path = "/customers")
    public ResponseEntity<?> getCustomers() {
        List<Customer> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }

    @ApiOperation("Returns a specific customer by id. 404 if does not exist.")
    @GetMapping(path = "/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable("id") String id) {
        try {
            LOGGER.info("CustomerController::: " + id);
            Customer result = customerService.getCustomer(id);

            return ResponseEntity.ok(result);
        }catch (RuntimeException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Resource Not Found", exc);
        }
    }

    @ApiOperation("Creates a new customer.")
    @PostMapping(path = "/customers")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO customerDTO) {
        LOGGER.info("CustomerController: " + customerDTO);
        Customer result = customerService.saveCustomer(customerDTO);

        return ResponseEntity.ok(result);
    }

    @ApiOperation("Updates and existing customer.")
    @PutMapping(path = "/customer")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        LOGGER.info("CustomerController: " + customerDTO);
        Customer customer = customerService.updateCustomer(customerDTO);

        return ResponseEntity.ok(customer);
    }

    @ApiOperation("Deletes a customer. 404 if the customer's identifier is not found.")
    @DeleteMapping(path = "/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") String id) {
        LOGGER.info("CustomerController: " + id);
        String result = customerService.deleteCustomer(id);

        return ResponseEntity.ok(result);
    }
}
