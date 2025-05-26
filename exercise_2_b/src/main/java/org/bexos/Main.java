package org.bexos;

import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.explode;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: RecommendationEngine <ratings-csv-file> <movies-csv-file>");
            System.exit(1);
        }

        String ratingsFile = args[0];
        String moviesFile = args[1];

        try (SparkSession spark = SparkSession.builder()
                .appName("MovieRecommendationEngine")
                .master("local[*]")
                .getOrCreate()) {

            // Load user ratings
            Dataset<Row> ratings = spark.read()
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .csv(ratingsFile);

            // Load movies
            Dataset<Row> movies = spark.read()
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .csv(moviesFile);

            // Train ALS model
            ALS als = new ALS()
                    .setUserCol("userId")
                    .setItemCol("itemId")
                    .setRatingCol("rating")
                    .setMaxIter(10)
                    .setRegParam(0.1)
                    .setColdStartStrategy("drop");

            ALSModel model = als.fit(ratings);

            // Get top 3 recommendations for all users
            Dataset<Row> userRecs = model.recommendForAllUsers(3);

            // Explode recommendations into separate rows
            Dataset<Row> exploded = userRecs
                    .withColumn("rec", explode(col("recommendations")))
                    .select(
                            col("userId"),
                            col("rec.itemId").alias("itemId"),
                            col("rec.rating").alias("predictedRating")
                    );

            // Join with movie titles
            Dataset<Row> finalRecs = exploded
                    .join(movies, "itemId")
                    .select("userId", "title", "predictedRating")
                    .orderBy(col("userId"), col("predictedRating").desc());

            finalRecs.show(false);

        }
    }
}