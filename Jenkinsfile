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

        stage('Run Tests') {
            steps {
                script {
                    // Run the tests in the Docker container
                    docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").inside {
                        sh 'mvn test'
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker images
            script {
                sh 'docker image prune -af'
            }
        }
    }
}
