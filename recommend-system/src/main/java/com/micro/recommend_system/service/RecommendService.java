package com.micro.recommend_system.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RecommendService {
    @Autowired
    private SparkSession sparkSession;

    private ALSModel model;

    @PostConstruct
    public void trainModel() {
        Dataset<Row> ratings = loadingData();
        model = trainALSModel(ratings);
    }

    private Dataset<Row> loadingData() {
        String path = "data/ratings.csv";

        return sparkSession.read().format("csv").option("header", "true").option("inferSchema", "true").load(path);
    }

    private ALSModel trainALSModel(Dataset<Row> ratings) {
        ALS als = new ALS().setMaxIter(10).setRegParam(0.1).setUserCol("userId").setItemCol("productId")
                .setRatingCol("rating");

        ALSModel model = als.fit(ratings);
        model.setColdStartStrategy("drop");

        return model;
    }

    public List<String> getRecommendations(Long userId) {
        Dataset<Row> users = sparkSession.createDataFrame(Collections.singletonList(userId), Long.class).toDF("userId");

        Dataset<Row> recommendations = model.recommendForUserSubset(users, 10);

        List<Row> rows = recommendations.collectAsList();

        if (rows.isEmpty()) {
            return Collections.emptyList();
        }

        Row row = rows.get(0);
        List<Row> recs = row.getList(1);
        List<String> productIds = new ArrayList<>();

        for (Row rec : recs) {
            productIds.add(rec.getAs(0).toString());
        }

        return productIds;

    }

}
