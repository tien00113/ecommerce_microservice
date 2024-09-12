// package com.micro.product_service.models;

// import java.util.Collections;
// import java.util.List;

// import org.apache.spark.ml.recommendation.ALSModel;
// import org.apache.spark.sql.Dataset;
// import org.apache.spark.sql.Encoders;
// import org.apache.spark.sql.Row;
// import org.apache.spark.sql.SparkSession;

// public class RecommendationModel {
//     private final SparkSession sparkSession;
//     private final String modelPath;
//     private ALSModel model;

//     public RecommendationModel(SparkSession sparkSession, String modelPath) {
//         this.sparkSession = sparkSession;
//         this.modelPath = modelPath;
//         this.model = loadModel();
//     }

//     private ALSModel loadModel() {
//         return ALSModel.load(modelPath);
//     }

//     public ALSModel getModel() {
//         return model;
//     }

//     public void reloadModel() {
//         this.model = loadModel();
//     }

//     public boolean isModelLoaded() {
//         return model != null;
//     }

//     public Dataset<Row> recommendProductsForUser(Long userId, int numRecommendations) {
//         List<Long> userList = Collections.singletonList(userId);
//         Dataset<Long> userDataset = sparkSession.createDataset(userList, Encoders.LONG());
//         Dataset<Row> userDataFrame = userDataset.toDF("userId");
//         Dataset<Row> userRecs = model.recommendForUserSubset(userDataFrame, numRecommendations);
//         return userRecs.select("recommendations");
//     }

//     public String getModelPath() {
//         return modelPath;
//     }
// }
