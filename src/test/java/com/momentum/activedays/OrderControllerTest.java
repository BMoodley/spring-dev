package com.momentum.activedays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momentum.activedays.controller.OrderController;
import com.momentum.activedays.controller.ProductController;
import com.momentum.activedays.dto.OrderDTO;
import com.momentum.activedays.entity.Order;
import com.momentum.activedays.entity.Product;
import com.momentum.activedays.service.CustomerService;
import com.momentum.activedays.service.OrderService;
import com.momentum.activedays.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private OrderService service;

    @Test
    public void customerShouldNotBeAbleToPlaceOrderIfLessPoints() throws Exception {

        List<String> products = new ArrayList<>();
        products.add("");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer("");
        orderDTO.setProducts(products);

        when(service.purchaseOrder(orderDTO)).thenReturn(null);
//        this.mockMvc.perform(post("/order/orders")).andDo(print()).andExpect(status().isNotFound());

        mockMvc.perform( MockMvcRequestBuilders
                .post("/order/orders")
                .content(asJsonString(orderDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFailedDependency());
    }

    @Test
    public void customerShouldBeAbleToPlaceOrderIfLessPoints() throws Exception {

        List<String> products = new ArrayList<>();
        products.add("");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomer("");
        orderDTO.setProducts(products);

        when(service.purchaseOrder(orderDTO)).thenReturn(new Order());

        mockMvc.perform( MockMvcRequestBuilders
                .post("/order/orders")
                .content(asJsonString(orderDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
