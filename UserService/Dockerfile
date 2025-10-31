# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom and source, Doing offline to cache dependencies in middle
COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

# Build the JAR
#RUN mvn clean package -DskipTests
RUN mvn clean verify -DskipTests=false

# -Dspring.profiles.active=test

# Stage 2: Runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y postgresql-client

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
COPY wait-for-postgres.sh /app/wait-for-postgres.sh
RUN chmod +x wait-for-postgres.sh

# Expose port
EXPOSE 8124

# Run the app
ENTRYPOINT ["sh", "-c", "./wait-for-postgres.sh postgres && java -jar app.jar"]
