# Use an official Maven image as the base image
FROM maven:3.8.6-openjdk-11

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and the source code into the container
COPY pom.xml ./
COPY src ./src

# Command to run the tests (assuming tests are in the src/test/java directory)
CMD ["mvn", "test"]
