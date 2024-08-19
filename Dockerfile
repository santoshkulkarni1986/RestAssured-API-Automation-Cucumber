# Use an official Maven image as a parent image
FROM maven:3.8.6-openjdk-11

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the pom.xml and source code into the container
COPY pom.xml ./
COPY src ./src

# Download the dependencies, build the project, and run the tests
RUN mvn install

# The command to keep the container alive (optional, if needed)
CMD ["tail", "-f", "/dev/null"]
