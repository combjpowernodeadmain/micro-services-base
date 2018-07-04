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
                withMaven(globalMavenSettingsConfig: 'c79fc3fc-9de4-4dfb-bfd0-bf179d7950c2', jdk: 'JDK8', maven: 'MAVEN', mavenSettingsConfig: '4557b50b-90a1-4723-8dac-40554c29de07', publisherStrategy: 'EXPLICIT') {
                    sh 'mvn clean install deploy -Dmven.test.skip=true'   
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
