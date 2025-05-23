pipeline {
    agent {
        docker {
            image 'maven:3.8.6-openjdk-17'
            args '--network host --volume /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=512m'
    }

    stages {
        stage('Code Validation') {
            steps {
                sh '''
                mvn checkstyle:check
                mvn pmd:check
                mvn dependency-check:check
                '''
            }
        }

        stage('Build and Test') {
            steps {
                sh '''
                mvn clean install
                mvn test
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                docker build -t your-dockerhub-username/hit-it-off-backend:latest .
                '''
            }
        }

        stage('Docker Push') {
            steps {
                sh '''
                docker push your-dockerhub-username/hit-it-off-backend:latest
                '''
            }
        }
    }
}
