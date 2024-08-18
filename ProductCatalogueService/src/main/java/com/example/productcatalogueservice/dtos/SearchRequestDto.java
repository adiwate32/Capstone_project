package com.example.productcatalogueservice.dtos;

import com.example.productcatalogueservice.models.SortParam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRequestDto {
    private Integer pageLimit;
    private Integer pageNumber;
    private String query;
    List<SortParam> sortParams;
}
