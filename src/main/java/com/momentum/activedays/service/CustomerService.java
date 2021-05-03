package com.momentum.activedays.service;

import com.momentum.activedays.dto.CustomerDTO;
import com.momentum.activedays.entity.Customer;
import com.momentum.activedays.repository.CustomerRepository;
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
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService() {}

    @Timed(value = "customers.time", description = "Time taken to return all customers from mongodb")
    public List<Customer> getCustomers() {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> customerRepository.findAll(),
                throwable -> getDefaultCustomer());
    }

    /**
     * Fallback method when getCustomers fails to fetch data from mongodb
     * We should usually implement caching like Redis,
     * but for this project we will use sample data to demonstrate
     * @return fallback list of customers
     */
    public List<Customer> getDefaultCustomer() {
//        Should implement caching mechanism as a failover approach
        Customer customer = new Customer();
        customer.setId("1234");
        customer.setName("John Doe");
        customer.setPoints(2000);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        return customers;
    }

    /**
     * fetches the customers current points from the database
     * @param id a customers id as String
     * @return the customers points
     */
    public Integer getCustomerPoints(String id) {
        Optional<Customer> customerResult = customerRepository.findById(id);
        Customer customer = customerResult.get();
        LOGGER.info("CustomerService: getCustomerPoints: points" + customer.getPoints());
        return customer.getPoints();
    }

    public Customer getCustomer(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.get();
    }

    public Customer saveCustomer(CustomerDTO customerDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Customer product = modelMapper.map(customerDTO, Customer.class);
        return customerRepository.save(product);
    }

    public Customer updateCustomer(CustomerDTO customerDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        return customerRepository.save(customer);
    }

    public String deleteCustomer(String id) {
        customerRepository.deleteById(id);
        return id;
    }
}
