package com.momentum.activedays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.momentum.activedays.controller.ProductController;
import com.momentum.activedays.entity.Product;
import com.momentum.activedays.service.ProductService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    public void shouldReturnListOfProducts() throws Exception {

        Product product = new Product();
        product.setId("1234");
        product.setCode("defcode");
        product.setName("Default Product");
        product.setPoints(0);
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(service.getProducts()).thenReturn(products);
        this.mockMvc.perform(get("/product/products")).andDo(print()).andExpect(status().isOk());
    }

}
