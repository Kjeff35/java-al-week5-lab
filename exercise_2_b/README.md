# Recommendation Engine with Apache Spark
This project demonstrates how to build a basic collaborative filtering-based movie recommendation engine using Apache Spark's MLlib (ALS algorithm).

## Features
- Loads user-item rating data from a CSV file
- Uses the ALS (Alternating Least Squares) algorithm for collaborative filtering
- Generates top-N movie recommendations for each user
- Joins recommended movie IDs with actual movie titles
---

## Overview

The application reads user-item rating data from a CSV file, trains a collaborative filtering model using ALS, and generates product recommendations for users.

---

## Prerequisites

- Java 8 or higher
- Apache Spark 3.x installed and configured
- Maven (for building the project)
- `ratings.csv` file containing user ratings
- `movies.csv` file containing movie title

---

## Input Data Format

The input CSV file should have the following columns:
- Rating CSV file

| userId | itemId | rating |
|--------|--------|--------|
| 1      | 101    | 5.0    |
| 2      | 102    | 3.0    |
| ...    | ...    | ...    |

- Movie CSV file

| itemId | tile       |
|--------|------------|
| 101    | The Matrix |
| 102    | Inception  |
| ...    | ...        |

---

## Building the Project

Use Maven to build the project and package dependencies:

```bash
mvn clean package
```

## Run the project
```bash
spark-submit --class org.bexos.Main --master local[*] target/*.jar ./ratings.csv ./movies.csv
```

## Example Output (Human-Readable)
| userId | title           | predictedRating |
|--------|-----------------|-----------------|
| 1      | The Matrix      | 4.83            |
| 1      | Inception       | 3.02            |
| 1      | Interstellar    | 2.42            |
| 2      | The Matrix      | 3.90            |
| 2      | Inception       | 2.64            |
| 2      | The Dark Knight | 2.18            |
...
