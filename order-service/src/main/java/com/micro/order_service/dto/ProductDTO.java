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
    private Long stock;
    private String sizes;
    private Boolean active;
    private Long price;
    private List<ImageColorDTO> images;

    public ProductDTO(Long id, Long categoryId, String name, String description, Long stock, String sizes, Boolean active, Long price, List<ImageColorDTO> images) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.sizes = sizes;
        this.active = active;
        this.price = price;
        this.images = images;
    }
}
