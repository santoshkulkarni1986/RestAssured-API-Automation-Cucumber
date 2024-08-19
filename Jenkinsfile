pipeline {
    agent any

    environment {
        // Define environment variables if needed
        DOCKER_IMAGE = 'api-automation'
        DOCKER_TAG = 'latest'
    }

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image with a specific tag
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
    }
}
