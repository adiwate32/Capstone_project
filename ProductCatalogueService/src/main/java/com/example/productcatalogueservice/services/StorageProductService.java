package com.example.productcatalogueservice.services;

import com.example.productcatalogueservice.dtos.UserDto;
import com.example.productcatalogueservice.models.Product;
import com.example.productcatalogueservice.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class StorageProductService implements IProductService{

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Product getProductById(Long id) {
        Optional<Product> productOptional = productRepo.findById(id);
        return productOptional.orElse(null);
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return null;
    }

    @Override
    public Product getProductsBasedOnUserScope(Long pid, Long uid) {
        Product product = productRepo.findById(pid).orElse(null);
        UserDto userDto = restTemplate.getForEntity("http://userservice/users/{uid}", UserDto.class, uid).getBody();
        System.out.println("Email: " + userDto.getEmail());
        return product;
    }
}
