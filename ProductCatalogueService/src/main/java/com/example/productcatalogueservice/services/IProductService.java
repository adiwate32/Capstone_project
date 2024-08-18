package com.example.productcatalogueservice.services;

import com.example.productcatalogueservice.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    Product getProductById(Long id);

    List<Product> getAllProducts();

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    Product getProductsBasedOnUserScope(Long pid, Long uid);
}
