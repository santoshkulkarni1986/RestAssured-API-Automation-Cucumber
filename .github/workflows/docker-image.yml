# Use an official Maven image as a parent image
FROM maven:3.8.6-openjdk-11

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the pom.xml and source code into the container
COPY pom.xml ./
COPY src ./src

# Download the dependencies
RUN mvn dependency:resolve

# Run the tests and generate the reports
RUN mvn install

# Keep the container running (optional for debugging)
CMD ["tail", "-f", "/dev/null"]
