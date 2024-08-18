package com.example.productcatalogueservice.services;

import com.example.productcatalogueservice.models.Product;
import com.example.productcatalogueservice.models.SortParam;
import com.example.productcatalogueservice.models.SortType;
import com.example.productcatalogueservice.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService implements ISearchService {
    @Autowired
    private ProductRepo productRepo;

    @Override
    public Page<Product> searchProducts(String query, int pageNumber, int pageSize, List<SortParam> sortParams) {
        Sort sort = null;
        if(!sortParams.isEmpty()) {
            if(sortParams.get(0).getSortType().equals(SortType.ASC)){
                sort = Sort.by(sortParams.get(0).getParamName());
            }else{
                sort = Sort.by(sortParams.get(0).getParamName()).descending();
            }
        }
        for(int i=1; i<sortParams.size(); i++){
            if(sortParams.get(i).getSortType().equals(SortType.ASC)){
                sort.and(Sort.by(sortParams.get(i).getParamName()));
            }else{
                sort.and(Sort.by(sortParams.get(i).getParamName()).descending());
            }
        }
        return productRepo.findProductByName(query, PageRequest.of(pageNumber, pageSize, sort));
    }
}
