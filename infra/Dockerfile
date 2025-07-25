# Build stage
FROM maven:3.9.6-eclipse-temurin-21 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ARG PORT=8080
ENV PORT=$PORT
EXPOSE $PORT
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"] 