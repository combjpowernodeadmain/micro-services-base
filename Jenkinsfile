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
                                execCommand: 'kill -9 $(cat /data/scp/1_center/tpid) && kill -9 $(cat /data/scp/2_auth/tpid) && kill -9 $(cat /data/scp/3_dict/tpid) && kill -9 $(cat /data/scp/4_admin/tpid) && kill -9 $(cat /data/scp/5_gate/tpid) && kill -9 $(cat /data/scp/6_cgp/tpid)', 
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
                                execCommand: 'mv /data/scp/1_center/scp-center.jar /data/scp/1_center/scp-center.${BUILD_ID}.jar && mv /data/scp/2_auth/scp-auth.jar /data/scp/2_auth/scp-auth.${BUILD_ID}.jar && mv /data/scp/3_dict/scp-dict.jar /data/scp/3_dict/scp-dict.${BUILD_ID}.jar && mv /data/scp/4_admin/scp-admin.jar /data/scp/4_admin/scp-admin.${BUILD_ID}.jar && mv /data/scp/5_gate/scp-gate.jar /data/scp/5_gate/scp-gate.${BUILD_ID}.jar && mv /data/scp/6_cgp/scp-cgp.jar /data/scp/6_cgp/scp-cgp.${BUILD_ID}.jar', 
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
                                remoteDirectory: '1_center', 
                                remoteDirectorySDF: false, 
                                removePrefix: 'scp-center/target/', 
                                sourceFiles: 'scp-center/target/scp-center.jar'),
                            sshTransfer(excludes: '', 
                                execCommand: '', 
                                execTimeout: 120000, 
                                flatten: false, 
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '2_auth', 
                                remoteDirectorySDF: false, 
                                removePrefix: 'scp-auth/scp-auth-server/target/', 
                                sourceFiles: 'scp-auth/scp-auth-server/target/scp-auth.jar'),
                            sshTransfer(
                                excludes: '', 
                                execCommand: '', 
                                execTimeout: 120000, 
                                flatten: false, 
                                makeEmptyDirs: false, 
                                noDefaultExcludes: false, 
                                patternSeparator: '[, ]+', 
                                remoteDirectory: '3_dict', 
                                remoteDirectorySDF: false, 
                                removePrefix: 'scp-modules/scp-dict/target', 
                                sourceFiles: 'scp-modules/scp-dict/target/scp-dict.jar'
                            ),
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
                                flatten: false, makeEmptyDirs: false, 
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
                                execCommand: 'cd /data/scp/1_center && ./start_center.sh', 
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

                sleep 20

                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'cd /data/scp/2_auth && ./start_auth.sh', 
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

                sleep 40

                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'cd /data/scp/3_dict && ./start_dict.sh', 
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

                sleep 40

                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'cd /data/scp/4_admin && ./start_admin.sh', 
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

                sleep 40
                
                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'cd /data/scp/5_gate && ./start_gate.sh', 
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

                sleep 40

                sshPublisher(publishers: [
                    sshPublisherDesc(
                        configName: 'AliyunServerZZ001', 
                        transfers: [
                            sshTransfer(
                                excludes: '', 
                                execCommand: 'cd /data/scp/6_cgp && ./start_cgp.sh', 
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
    }
}
