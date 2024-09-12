package com.micro.product_service.request;

import java.util.List;

import com.micro.product_service.request.ProductFilterRequest.PageableDTO.SortDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {
    private Integer minPrice;
    private Integer maxPrice;
    private Long categoryId;
    private String color;
    private PageableDTO pageableDTO;

    public ProductFilterRequest(Integer minPrice, Integer maxPrice, Long categoryId, String color, int page, int size,
            SortDTO sort) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.categoryId = categoryId;
        this.color = color;
        this.pageableDTO = new PageableDTO(page, size, sort);
    }

    @Setter
    @Getter
    public static class PageableDTO {
        private int size;
        private int page;
        // private List<SortDTO> sort;
        private SortDTO sort;

        public PageableDTO(int size, int page, SortDTO sort) {
            this.size = size;
            this.page = page;
            this.sort = sort;
        }

        @Getter
        @Setter
        public static class SortDTO {
            private String property;
            private String direction;

            public SortDTO(String property, String direction) {
                this.property = property;
                this.direction = direction;
            }
        }
    }
}