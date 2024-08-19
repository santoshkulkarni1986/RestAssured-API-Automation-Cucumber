pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'api-automation'
        DOCKER_TAG = 'latest'
    }

    stages {
        stage('Build and Test Docker Image') {
            steps {
                script {
                    // Build the Docker image which runs the tests as part of the build process
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
    }


}