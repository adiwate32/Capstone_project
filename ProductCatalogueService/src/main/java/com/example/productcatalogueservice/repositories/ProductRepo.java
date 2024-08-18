package com.example.productcatalogueservice.repositories;

import com.example.productcatalogueservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findProductByPriceBetween(int lower, int higher);

    @Query("select p.name from Product p where p.id=:productId")
    String findProductNameFromId(Long productId);

    @Query("select c.name from Product p join Category c on p.category.id = c.id where p.id = :productId")
    String findCategoryNameFromProductId(Long productId);

    Page<Product> findProductByName(String query, Pageable pageable);
}
