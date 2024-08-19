# Use an official Maven image to build the project
FROM maven:3.8.6-openjdk-11 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .
COPY src ./src

# Install dependencies and build the project
RUN mvn clean install

# Run the tests
CMD ["mvn", "test"]
# Use a lightweight JRE image to run the application
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/Api-Automation-RestAssured-Cucumber-0.0.1-SNAPSHOT.jar /app/Api-Automation-RestAssured-Cucumber.jar

# Set the command to run the JAR file
#CMD ["java", "-jar", "Api-Automation-RestAssured-Cucumber.jar"]
