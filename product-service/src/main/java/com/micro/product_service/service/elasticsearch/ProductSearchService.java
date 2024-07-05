package com.micro.product_service.service.elasticsearch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.repository.ProductElasticsearchRepository;

@Service
public class ProductSearchService {

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    public Page<ProductDTO> searchProductsFuzzy(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productElasticsearchRepository.findByNameWithFuzzy(name, pageable);
    }

    public List<ProductDTO> searchProducts(String name){
        return productElasticsearchRepository.findByName(name);
    }
}
