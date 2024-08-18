package com.example.productcatalogueservice.services;

import com.example.productcatalogueservice.dtos.FakeStoreProductDto;
import com.example.productcatalogueservice.models.Category;
import com.example.productcatalogueservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
//@Primary
public class FakeProductService implements IProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Product getProductById(Long id) {
        FakeStoreProductDto fakeStoreProductDto = null;
        fakeStoreProductDto = (FakeStoreProductDto) redisTemplate.opsForHash().get("_PRODUCTS", id);
        if (fakeStoreProductDto != null) {
            System.out.println("Found product with id: " + id);
            return from(fakeStoreProductDto);
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/" + id, FakeStoreProductDto.class);
        if(responseEntity.getBody() != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            System.out.println("Found by calling fake store id :" + id);
            redisTemplate.opsForHash().put("_PRODUCTS", id, responseEntity.getBody());
            return from(responseEntity.getBody());
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForEntity("https://fakestoreapi.com/products/", FakeStoreProductDto[].class).getBody();
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos) {
            Product product = from(fakeStoreProductDto);
            products.add(product);
        }
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        FakeStoreProductDto fakeStoreProductDtoReq = from(product);
        ResponseEntity<FakeStoreProductDto> responseEntity = requestForEntity("https://fakestoreapi.com/products/" + id, fakeStoreProductDtoReq, FakeStoreProductDto.class);
        if(responseEntity.getBody() != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return from(responseEntity.getBody());
        }
        return null;
    }

    @Override
    public Product getProductsBasedOnUserScope(Long pid, Long uid) {
        return null;
    }

    public <T> ResponseEntity<T> requestForEntity(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor, uriVariables);
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto){
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setImageUri(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private FakeStoreProductDto from(Product product){
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setImage(product.getImageUri());
        if(product.getCategory() != null) {
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        return fakeStoreProductDto;
    }
}
