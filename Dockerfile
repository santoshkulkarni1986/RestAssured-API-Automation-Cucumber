# Use an official Maven image as a parent image
FROM maven:3.9.2-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files into the container
COPY pom.xml .
COPY src /app/src

# Build the Maven project
RUN mvn clean install

# Create a new stage to reduce the final image size
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven dependencies and built JAR files from the build stage
COPY --from=build /app/target/ /app/

# Copy the Maven project files into the container
COPY --from=build /app/pom.xml /app/

# Set the entrypoint to run the Maven test goal
ENTRYPOINT ["mvn", "test"]
