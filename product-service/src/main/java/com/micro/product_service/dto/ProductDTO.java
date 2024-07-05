package com.micro.product_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "products")
public class ProductDTO {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private long stock;
    private List<String> sizes;
    private List<String> colors;
    private Boolean active;
    private long price;
    private List<ProductVariantDTO> variants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // private List<ImageColorDTO> images;

    public ProductDTO(Long id, Long categoryId, String name, String description, long stock, List<String> sizes, List<String> colors, Boolean active, long price, List<ProductVariantDTO> variantDTOList, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.sizes = sizes;
        this.colors = colors;
        this.active = active;
        this.price = price;
        this.variants = variantDTOList;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        // this.images = images;
    }

}
