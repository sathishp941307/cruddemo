package com.example.studentdemo.controller;

import com.example.studentdemo.model.Product;
import com.example.studentdemo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setPrice(BigDecimal.valueOf(100.0));
    }

    @Test
    void testGetAllProduct() {
        List<Product> mockProductList = new ArrayList<>();
        mockProductList.add(mockProduct);

        when(productService.getAllProduct()).thenReturn(mockProductList);

        ResponseEntity<List<Product>> response = productController.getAllProduct();

        assertEquals(1, response.getBody().size());
        assertEquals(mockProduct, response.getBody().get(0));
        verify(productService, times(1)).getAllProduct();
    }

    @Test
    void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(mockProduct);

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(mockProduct, response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testCreateProduct() {
        when(productService.createProduct(mockProduct)).thenReturn(mockProduct);

        ResponseEntity<Product> response = productController.createProduct(mockProduct);

        assertEquals(mockProduct, response.getBody());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService, times(1)).createProduct(mockProduct);
    }

    @Test
    void testUpdateProduct() {
        mockProduct.setName("Updated Product");

        when(productService.updateProduct(mockProduct)).thenReturn(mockProduct);

        ResponseEntity<Product> response = productController.updateProduct(1L, mockProduct);

        assertEquals(mockProduct, response.getBody());
        assertEquals("Updated Product", response.getBody().getName());
        verify(productService, times(1)).updateProduct(mockProduct);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        HttpStatus response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.OK, response);
        verify(productService, times(1)).deleteProduct(1L);
    }
}