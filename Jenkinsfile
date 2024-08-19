pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'kushi-santosh-api-automation'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from Git repository
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run the Docker container and execute Maven tests
                    sh "docker run --rm ${DOCKER_IMAGE}"
                }
            }
        }
    }

    post {
        always {
            script {
                if (isUnix()) {
                    // Clean up Docker images and containers for Unix-based agents
                    sh "docker system prune -f"
                } else {
                    // Windows-specific cleanup or notification
                    echo "Docker cleanup not supported on Windows or not configured."
                }
            }
        }
    }
}
