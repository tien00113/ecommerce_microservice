// package com.micro.product_service.service.recommend;

// import java.io.IOException;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.apache.spark.ml.recommendation.ALS;
// import org.apache.spark.ml.recommendation.ALSModel;
// import org.apache.spark.sql.Dataset;
// import org.apache.spark.sql.Row;
// import org.apache.spark.sql.RowFactory;
// import org.apache.spark.sql.SparkSession;
// import org.apache.spark.sql.types.DataTypes;
// import org.apache.spark.sql.types.Metadata;
// import org.apache.spark.sql.types.StructField;
// import org.apache.spark.sql.types.StructType;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.micro.product_service.models.Interaction;
// import com.micro.product_service.models.Product;
// import com.micro.product_service.models.RecommendationModel;
// import com.micro.product_service.repository.InteractionRepository;
// import com.micro.product_service.service.ProductService;

// // @Service
// public class RecommendationService {

//     @Autowired
//     private InteractionRepository interactionRepository;

//     @Autowired
//     private ProductService productService;

//     @Autowired
//     private SparkSession sparkSession;

//     @Autowired
//     private RecommendationModel recommendationModel;

//     public List<Product> recommendItemsForUser(Long userId, int numRecommendations) throws IOException {
//         // Train ALS model if it's not already loaded
//         if (!recommendationModel.isModelLoaded()) {
//             trainALSModel();
//         }

//         // Fetch recommendations using the trained ALS model
//         Dataset<Row> userRecs = recommendationModel.recommendProductsForUser(userId, numRecommendations);

//         // Example: Convert Dataset<Row> to List<Product>
//         List<Product> recommendedProducts = userRecs.collectAsList().stream()
//                 .map(row -> {
//                     Long productId = row.getLong(1); // Assuming the first column is productId
//                     Product product = productService.findProductById(productId);
//                     return product;
//                 })
//                 .collect(Collectors.toList());

//         return recommendedProducts;
//     }

//     public void trainALSModel() throws IOException {
//         // Retrieve interactions data
//         List<Interaction> allInteractions = interactionRepository.findAll();

//         // Prepare data for ALS model training
//         List<Row> rows = allInteractions.stream()
//                 .map(interaction -> RowFactory.create(interaction.getUserId(), interaction.getProductId(), interaction.getRating()))
//                 .collect(Collectors.toList());

//         StructType schema = new StructType(new StructField[]{
//                 new StructField("userId", DataTypes.LongType, false, Metadata.empty()),
//                 new StructField("productId", DataTypes.LongType, false, Metadata.empty()),
//                 new StructField("rating", DataTypes.DoubleType, false, Metadata.empty())
//         });

//         Dataset<Row> ratingsDF = sparkSession.createDataFrame(rows, schema);

//         // Define ALS model parameters
//         ALS als = new ALS()
//                 .setMaxIter(10)         // Số lần lặp tối đa
//                 .setRegParam(0.1)       // Tham số regularization
//                 .setUserCol("userId")   // Tên cột chứa userId trong DataFrame
//                 .setItemCol("productId")// Tên cột chứa productId trong DataFrame
//                 .setRatingCol("rating"); // Tên cột chứa rating trong DataFrame

//         // Fit ALS model to the data
//         ALSModel alsModel = als.fit(ratingsDF);

//         // Save the trained model
//         alsModel.save(recommendationModel.getModelPath());

//         // Reload the model into RecommendationModel instance
//         recommendationModel.reloadModel();
//     }
// }
