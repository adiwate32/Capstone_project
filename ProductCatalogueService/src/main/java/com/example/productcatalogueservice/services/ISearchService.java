package com.example.productcatalogueservice.services;

import com.example.productcatalogueservice.models.Product;
import com.example.productcatalogueservice.models.SortParam;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISearchService {
    Page<Product> searchProducts(String searchTerm, int pageNumber, int pageSize, List<SortParam> sortParams);
}
