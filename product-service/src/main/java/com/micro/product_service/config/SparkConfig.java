// package com.micro.product_service.config;

// import org.apache.spark.SparkConf;
// import org.apache.spark.api.java.JavaSparkContext;
// import org.apache.spark.sql.SparkSession;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.micro.product_service.models.RecommendationModel;

// @Configuration
// public class SparkConfig {

//     @Bean
//     public SparkConf sparkConf() {
//         return new SparkConf()
//                 .setAppName("RecommendationSystem")
//                 .setMaster("local[3]")
//                 .set("spark.hadoop.io.nativeio", "false") // Tắt NativeIO
//                 .set("spark.sql.warehouse.dir", "file:///D:/temp");
//     }

//     @Bean
//     public JavaSparkContext javaSparkContext() {
//         return new JavaSparkContext(sparkConf());
//     }

//     // @Bean
//     // public SparkSession sparkSession() {
//     //     return SparkSession.builder().appName("RecommendationService").master("local[3]").getOrCreate();
//     // }

//     @Bean
//     public SparkSession sparkSession() {
//         return SparkSession.builder()
//                 .sparkContext(javaSparkContext().sc())
//                 .appName("RecommendationSystem")
//                 .getOrCreate();
//     }

//     @Bean
//     public RecommendationModel recommendationModel(SparkSession sparkSession) {
//         String modelPath = "./als-models";
//         return new RecommendationModel(sparkSession, modelPath);
//     }
// }   
