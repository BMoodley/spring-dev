package com.momentum.activedays.links;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class ProductLinks {
    public static final String PRODUCTS = "/products";
    public static final String PRODUCT = "/product/{id}";
    public static final String CREATE_PRODUCT = "/products";
    public static final String UPDATE_PRODUCT = "/product";
    public static final String DELETE_PRODUCT = "/product/{id}";
}
