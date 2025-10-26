# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom and source
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y default-mysql-client

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
COPY wait-for-mysql.sh /app/wait-for-mysql.sh
RUN chmod +x wait-for-mysql.sh

# Expose port
EXPOSE 8125

# Run the app
ENTRYPOINT ["sh", "-c", "./wait-for-mysql.sh mysql && java -jar app.jar"]
