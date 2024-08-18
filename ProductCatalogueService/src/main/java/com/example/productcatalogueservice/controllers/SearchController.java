package com.example.productcatalogueservice.controllers;

import com.example.productcatalogueservice.dtos.CategoryDto;
import com.example.productcatalogueservice.dtos.ProductDto;
import com.example.productcatalogueservice.dtos.SearchRequestDto;
import com.example.productcatalogueservice.models.Product;
import com.example.productcatalogueservice.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ISearchService searchService;

    @PostMapping
    public Page<Product> searchProducts(@RequestBody SearchRequestDto searchRequestDto) {
        return searchService.searchProducts(searchRequestDto.getQuery(), searchRequestDto.getPageNumber(), searchRequestDto.getPageLimit(), searchRequestDto.getSortParams());
    }
}
