pipeline {
    agent any

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build('santosh-kulkarni-api-automation', '-f Dockerfile .')
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    docker.image('santosh-kulkarni-api-automation').inside {
                        sh 'mvn test'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    docker.image('santosh-kulkarni-api-automation').inside {
                        sh 'java -jar target/Api-Automation-RestAssured-Cucumber-0.0.1-SNAPSHOT.jar'
                    }
                }
            }
        }
    }
}
