# Use an official OpenJDK image as a base
FROM openjdk:8-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml /app/
COPY src /app/src

# Install Maven
RUN apk add --no-cache maven

# Resolve dependencies and build the project
RUN mvn clean install

# Command to run the tests using Maven
CMD ["mvn", "test"]
