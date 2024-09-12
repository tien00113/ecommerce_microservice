package com.micro.product_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.micro.product_service.dto.ProductDTO;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDTO, Long>{

    // @Query("{\"bool\": {\"must\": {\"match\": {\"name\": \"?0\"}}}}")
    @Query("{\"match\": {\"name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<ProductDTO> findByName(String name);

    // @Query("{\"fuzzy\": {\"name\": {\"value\": \"?0\", \"fuzziness\": \"auto\"}}}")
    // List<ProductDTO> findByNameFuzzy(String name);

    // @Query("{\"match\": {\"name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    @Query("{\"match\": {\"name\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    Page<ProductDTO> findByNameWithFuzzy(String name, Pageable pageable);

}