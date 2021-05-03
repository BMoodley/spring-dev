package com.momentum.activedays.controller;

import com.momentum.activedays.dto.ProductDTO;
import com.momentum.activedays.entity.Product;
import com.momentum.activedays.links.ProductLinks;
import com.momentum.activedays.service.ProductService;
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
@RequestMapping("/product/")
@RequiredArgsConstructor
@Api(description = "Set of endpoints for Creating, Retrieving, Updating and Deleting of Products.")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @ApiOperation("Returns list of all Products in the system.")
    @GetMapping(path = ProductLinks.PRODUCTS)
    public ResponseEntity<?> getProducts() {
        List<Product> productList = productService.getProducts();
        return ResponseEntity.ok(productList);
    }

    @ApiOperation("Returns a specific product by id. 404 if does not exist.")
    @GetMapping(path = ProductLinks.PRODUCT)
    public ResponseEntity<?> getProduct(@PathVariable("id") String id) {
        try {
            LOGGER.info("ProductController::: " + id);
            Product result = productService.getProduct(id);

            return ResponseEntity.ok(result);
        }catch (RuntimeException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Resource Not Found", exc);
        }
    }

    @ApiOperation("Creates a new product.")
    @PostMapping(path = ProductLinks.CREATE_PRODUCT)
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        LOGGER.info("ProductController: " + productDTO);
        Product result = productService.saveProduct(productDTO);

        return ResponseEntity.ok(result);
    }

    @ApiOperation("Updates and existing product.")
    @PutMapping(path = ProductLinks.UPDATE_PRODUCT)
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO) {
        LOGGER.info("ProductController: " + productDTO);
        Product product = productService.updateProduct(productDTO);

        return ResponseEntity.ok(product);
    }

    @ApiOperation("Deletes a product. 404 if the product's identifier is not found.")
    @DeleteMapping(path = ProductLinks.DELETE_PRODUCT)
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        LOGGER.info("ProductController: " + id);
        String result = productService.deleteProduct(id);

        return ResponseEntity.ok(result);
    }
}
