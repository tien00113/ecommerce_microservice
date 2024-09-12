// package com.micro.product_service.controller;

// import java.io.IOException;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RestController;

// import com.micro.product_service.models.Product;
// import com.micro.product_service.service.recommend.RecommendationService;

// // @RestController
// public class RecommendationController {
//     @Autowired
//     private RecommendationService recommendationService;

//     @GetMapping("/{userId}")
//     public List<Product> recommendProductsForUser(@PathVariable Long userId) throws IOException {
//         int numRecommendations = 10; // Số lượng sản phẩm đề xuất
//         return recommendationService.recommendItemsForUser(userId, numRecommendations);
//     }
// }
