# Build stage
FROM maven:3.9-amazoncorretto-25 AS build
WORKDIR /app
COPY pom.xml .
# Pre-download dependencies for layer caching before source is available
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package

# Run stage
FROM amazoncorretto:25-al2023
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ARG PORT=8080
ENV PORT=$PORT
EXPOSE $PORT
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"] 
