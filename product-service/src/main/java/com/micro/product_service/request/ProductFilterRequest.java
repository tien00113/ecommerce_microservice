package com.micro.product_service.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {
    private int minPrice;
    private int maxPrice;
    private Long categoryId;
    private String color;
    private PageableDTO pageableDTO;


    @Setter
    @Getter
    public class PageableDTO {
        private int size;
        private int page;
        // private List<SortDTO> sort;
         private SortDTO sort;



        @Getter
        @Setter
        public static class SortDTO {
            private String property;
            private String direction;
        }
    }
}