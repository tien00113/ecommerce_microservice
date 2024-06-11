package com.micro.product_service.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long stock;
    private String sizes;
    private Boolean active;

    @ElementCollection
    @MapKeyColumn(name = "color")
    @Column(name = "image_url")
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    private Map<String, String> imageColorMap = new HashMap<>();

    public void addImageColor(String color, String imageUrl) {
        this.imageColorMap.put(color, imageUrl);
    }

    public String getImageByColor(String color) {
        return this.imageColorMap.get(color);
    }

    public List<String> getColorList() {
        return this.imageColorMap.keySet().stream().collect(Collectors.toList());
    }

    public List<String> getImageList() {
        return this.imageColorMap.values().stream().collect(Collectors.toList());
    }

    public Map<String, String> getImageColorMap() {
        return new HashMap<>(this.imageColorMap);
    }
}
