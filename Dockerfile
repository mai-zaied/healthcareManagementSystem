# Build the Spring Boot application
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY . .
RUN mvn -B -DskipTests clean package

# Run the JAR
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
