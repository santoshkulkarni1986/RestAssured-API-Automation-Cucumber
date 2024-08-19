pipeline {
    agent {
        docker {
            image 'maven:3.8.6-openjdk-11'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Test') {
            steps {
                // Run tests
                sh 'mvn test'
            }
        }
    }
    post {
        always {
            // Archive test results and other artifacts
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts 'target/*.jar'
        }
    }
}
