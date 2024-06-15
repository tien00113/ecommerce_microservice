package com.micro.order_service.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    // private List<ImageColorDTO> images;

    public ProductDTO(Long id, Long categoryId, String name, String description, long stock, List<String> sizes, List<String> colors, Boolean active, long price, List<ProductVariantDTO> variantDTOList) {
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
        // this.images = images;
    }

}
