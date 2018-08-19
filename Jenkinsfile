pipeline {
    agent any
    tools {
        maven 'MAVEN'
        jdk 'JDK8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo "JAVA_HOME = ${JAVA_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                withMaven(globalMavenSettingsConfig: 'MavenNexusLutuoSettings', jdk: 'JDK8', maven: 'MAVEN', mavenSettingsConfig: 'MavenNexusLutuoSettings', publisherStrategy: 'EXPLICIT') {
                    sh 'mvn clean install -Dmven.test.skip=true -U'   
                }
                
            }
            post {
                success {
                    sh 'echo success'
                }
            }
        }
    }
}
