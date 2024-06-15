package com.micro.order_service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant {
    private Long id;

    private String color;
    private String size;
    private Long quantity;
    private String imageUrl;
}
