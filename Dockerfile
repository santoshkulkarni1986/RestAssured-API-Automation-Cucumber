# Use an official OpenJDK 11 image as a base
FROM openjdk:11-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml /app/
COPY src /app/src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Resolve dependencies and build the project
RUN mvn install

# Command to run the tests using Maven
CMD ["mvn", "test"]
