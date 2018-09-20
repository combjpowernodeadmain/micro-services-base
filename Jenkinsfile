pipeline {
    agent any
    options {
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')
        preserveStashes buildCount: 5
    }
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
                    sh 'echo Build success'
                }
            }
        }

        stage ('Stop Services in aliyun') {
            steps {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'cd /data/scp && sudo ./stop-scp.sh', 
                                execTimeout: 120000, 
                                flatten: false, 
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '', 
                                remoteDirectorySDF: false, 
                                removePrefix: '', 
                                sourceFiles: ''
                            )
                        ], 
                        usePromotionTimestamp: false, 
                        useWorkspaceInPromotion: false, 
                        verbose: false
                    )
                ])

            }
        }

        stage ('Rename Files in aliyun') {
            steps {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'mv /data/scp/6_cgp/scp-cgp.jar /data/scp/6_cgp/scp-cgp.${BUILD_ID}.jar', 
                                execTimeout: 120000, 
                                flatten: false, 
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '', 
                                remoteDirectorySDF: false, 
                                removePrefix: '', 
                                sourceFiles: ''
                            )
                        ], 
                        usePromotionTimestamp: false, 
                        useWorkspaceInPromotion: false, 
                        verbose: false
                    )
                ])

            }
        }
        stage ('Copy Files To aliyun') {
            steps {
                sh 'echo Deploy to aliyun'
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                             
                            sshTransfer(excludes: '', 
                                execCommand: '', 
                                execTimeout: 120000, 
                                flatten: false, 
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '4_admin', 
                                remoteDirectorySDF: false, 
                                removePrefix: 'scp-modules/scp-admin/target/', 
                                sourceFiles: 'scp-modules/scp-admin/target/scp-admin.jar'
                            ), 
                            sshTransfer(excludes: '', 
                                execCommand: '', 
                                execTimeout: 120000, 
                                flatten: false,
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '5_gate', 
                                remoteDirectorySDF: false, 
                                removePrefix: 'scp-gate/scp-gate-server/target/', 
                                sourceFiles: 'scp-gate/scp-gate-server/target/scp-gate.jar'
                            ), 
                            sshTransfer(excludes: '', 
                                execCommand: '', 
                                execTimeout: 120000, 
                                flatten: false, 
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '6_cgp', 
                                remoteDirectorySDF: false, 
                                removePrefix: 'scp-modules/scp-cgp/target/', 
                                sourceFiles: 'scp-modules/scp-cgp/target/scp-cgp.jar'
                            )
                        ], 
                        usePromotionTimestamp: false, 
                        useWorkspaceInPromotion: false, 
                        verbose: false
                    )
                ])

            }
        }


        stage ('Start Services in aliyun') {
            steps {
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'cd /data/scp/ && sudo ./start-scp.sh', 
                                execTimeout: 480000, 
                                flatten: false, 
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '', 
                                remoteDirectorySDF: false, 
                                removePrefix: '', 
                                sourceFiles: ''
                            )
                        ], 
                        usePromotionTimestamp: false, 
                        useWorkspaceInPromotion: false, 
                        verbose: false
                    )
                ])
 
            }
        }
    }
}
