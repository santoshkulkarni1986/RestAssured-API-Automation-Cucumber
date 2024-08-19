# Use an official Maven image as a parent image
FROM maven:3.8.6-openjdk-11

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the pom.xml and source code into the container
COPY pom.xml ./
COPY src ./src

# Download the dependencies and build the project
RUN mvn install

# Command to run the tests (you might need to adjust the command depending on how you run your tests)
CMD ["mvn", "test"]
