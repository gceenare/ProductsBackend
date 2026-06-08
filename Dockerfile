# Multi-stage Dockerfile for ProductsBackend (Java 17, Maven build)
# Stage 1: Build with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only what we need for dependency resolution first
COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests package -DskipTests

# Stage 2: Create minimal runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=build /workspace/target/ProductsBackend-1.0-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
