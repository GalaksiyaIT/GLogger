pipeline {
    agent any
    tools {
        maven 'mvn'
    }
    stages {
        stage('Package .jar') {
            steps{
                withMaven(maven: 'mvn') {
                    sh "mvn clean install -f GLogger/pom.xml"
                }
           }
        }
        stage('Deploy') {
            steps{
                withMaven(maven: 'mvn') {
                    sh "mvn install deploy:deploy -f GLogger/pom.xml"
                }
           }
        }
    }
}
