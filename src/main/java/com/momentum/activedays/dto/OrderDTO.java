package com.momentum.activedays.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class OrderDTO {
    private String id;
    private String customer;
    private List<String> products;

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
