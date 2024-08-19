# Use an official OpenJDK image as a parent image
FROM openjdk:17-jdk AS build

# Set the working directory in the container
WORKDIR /app

# Install wget and unzip
RUN apt-get update && \
    apt-get install -y wget unzip

# Download and install Maven
RUN wget https://archive.apache.org/dist/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.zip && \
    unzip apache-maven-3.9.0-bin.zip && \
    mv apache-maven-3.9.0 /usr/local/apache-maven && \
    ln -s /usr/local/apache-maven/bin/mvn /usr/bin/mvn

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

# Set the entrypoint to run the Maven test goal
ENTRYPOINT ["mvn", "test"]
