package com.micro.product_service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.dto.ProductVariantDTO;
import com.micro.product_service.models.Product;
import com.micro.product_service.models.ProductVariant;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        List<String> sizes = product.getVariants().stream()
                .map(ProductVariant::getSize)
                .distinct()
                .collect(Collectors.toList());

        List<String> colors = product.getVariants().stream()
                .map(ProductVariant::getColor)
                .distinct()
                .collect(Collectors.toList());

        long stock = product.getVariants().stream()
                .mapToLong(ProductVariant::getQuantity)
                .sum();

        List<ProductVariantDTO> variantsDTO = toVariantDTOList(product.getVariants());

        return new ProductDTO(
                product.getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getDescription(),
                stock,
                sizes,
                colors,
                product.getActive(),
                product.getPrice(),
                variantsDTO);
    }

    public static Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();

        product.setId(productDTO.getId());
        // set Category
        product.setStock(productDTO.getStock());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setActive(productDTO.getActive());
        product.setPrice(productDTO.getPrice());

        List<ProductVariant> variants = toVariantEntityList(productDTO.getVariants(), product);

        product.setVariants(variants);

        return product;
    }

    private static List<ProductVariant> toVariantEntityList(List<ProductVariantDTO> variantDTOs, Product product) {
        if (variantDTOs == null) {
            return null;
        }

        return variantDTOs.stream()
                .map(dto -> {
                    ProductVariant variant = new ProductVariant();
                    variant.setColor(dto.getColor());
                    variant.setSize(dto.getSize());
                    variant.setQuantity(dto.getQuantity());
                    variant.setImageUrl(dto.getImageUrl());
                    variant.setProduct(product);
                    return variant;
                })
                .collect(Collectors.toList());
    }

    private static List<ProductVariantDTO> toVariantDTOList(List<ProductVariant> variants) {
        if (variants == null) {
            return null;
        }

        return variants.stream()
                .map(variant -> new ProductVariantDTO(
                        variant.getId(),
                        variant.getColor(),
                        variant.getSize(),
                        variant.getQuantity(),
                        variant.getImageUrl()))
                .collect(Collectors.toList());
    }
}
