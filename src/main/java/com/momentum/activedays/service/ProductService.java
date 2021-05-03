package com.momentum.activedays.service;

import com.momentum.activedays.controller.OrderController;
import com.momentum.activedays.dto.ProductDTO;
import com.momentum.activedays.entity.Product;
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
public class ProductService {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    public ProductService() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Timed(value = "products.time", description = "Time taken to return all products from mongodb")
    public List<Product> getProducts() {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> productsRepository.findAll(),
                throwable -> getDefaultProducts());
    }

    /**
     * Fallback method when getProducts fails to fetch data from mongodb
     * We should usually implement caching like Redis,
     * but for this project we will use sample data to demonstrate
     * @return fallback list of products
     */
    public List<Product> getDefaultProducts() {
//        Should implement caching mechanism as a failover approach
        Product product = new Product();
        product.setId("1234");
        product.setCode("defcode");
        product.setName("Default Product");
        product.setPoints(0);
        List<Product> products = new ArrayList<>();
        products.add(product);
        return products;
    }

    /**
     * Takes in as input a list of product id strings and
     * sums up the points of all the orders
     * @param productIds list of String of product ids
     * @return total calculated points of all products
     */
    public Integer getTotalPoints(List<String> productIds) {
        List<Product> products = (List<Product>) productsRepository.findAllById(productIds);
        LOGGER.info("ProductService: getTotalPoints: product ids" + products);
        int totalPoints = 0;
        for (Product product : products) {
            totalPoints += product.getPoints();
        }
        return totalPoints;
    }

    public Product getProduct(String id) {
        Optional<Product> product = productsRepository.findById(id);
        return product.get();
    }

    public Product saveProduct(ProductDTO productDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productDTO, Product.class);
        return productsRepository.save(product);
    }

    public Product updateProduct(ProductDTO productDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productDTO, Product.class);
        return productsRepository.save(product);
    }

    public String deleteProduct(String id) {
        productsRepository.deleteById(id);
        return id;
    }
}
