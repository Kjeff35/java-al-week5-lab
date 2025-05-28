# Build Application
FROM maven:3.9.6 As build

WORKDIR /app

COPY . .

RUN mvn package -DskipTests

# Serve Application
FROM openjdk:21-bullseye

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "--enable-preview", "-jar", "app.jar"]