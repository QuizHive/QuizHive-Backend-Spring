# Use the official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot application JAR file to the container
COPY target/*.war /app/quizhive-backend.war

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/quizhive-backend.war"]
