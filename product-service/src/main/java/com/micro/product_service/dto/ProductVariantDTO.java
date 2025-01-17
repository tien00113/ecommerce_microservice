package com.micro.product_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {
    private Long id;
    private String color;
    private String size;
    private int quantity;
    private String imageUrl;
    private long price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}