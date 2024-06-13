package com.micro.product_service.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.micro.product_service.dto.ImageColorDTO;
import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.models.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        List<ImageColorDTO> images = product.getImageColorMap().entrySet().stream()
                .map(entry -> new ImageColorDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new ProductDTO(
            product.getId(),
            product.getCategory().getId(),
            product.getName(),
            product.getDescription(),
            product.getStock(),
            product.getSizes(),
            product.getActive(),
            product.getPrice(),
            images
        );

    }

    public static Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        
        Map<String, String> imageColorMap = new HashMap<>();
        for (ImageColorDTO imageColor : productDTO.getImages()) {
            imageColorMap.put(imageColor.getColor(), imageColor.getImageUrl());
        }

        Product product = new Product();
        product.setId(productDTO.getId());
        // product.setCategory(productDTO.getCategory());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setSizes(productDTO.getSizes());
        product.setActive(productDTO.getActive());
        product.setPrice(productDTO.getPrice());
        product.setImageColorMap(imageColorMap);

        return product;
    }
}
