package com.example.productcatalogueservice.repositories;

import com.example.productcatalogueservice.models.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Test
    @Transactional
    public void findProductByPriceBetween() {
        List<Product> productList = productRepo.findProductByPriceBetween(6499, 70000);
    }

    @Test
    @Transactional
    void findProductNameFromId() {
        String productName = productRepo.findProductNameFromId(1L);
    }

    @Test
    void findCategoryNameFromProductId() {
        String categoryName = productRepo.findCategoryNameFromProductId(1L);
        System.out.println(categoryName);
    }
}