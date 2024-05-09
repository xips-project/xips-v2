pipeline {
    agent any
    environment {
        DOCKER_USERNAME = credentials('DOCKER_USERNAME')
        DOCKER_TOKEN = credentials('DOCKER_TOKEN')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Set up JDK 17') {
            steps {
                script {
                    tool 'jdk-17'
                }
            }
        }
        stage('Build and run Sonar') {
            steps {
                script {
                    tool 'maven'
                    sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn verify sonar:sonar -Pcoverage -Dsonar.token=99ca41e7cdcf8d690af802b3917bbe26f2c716d8 -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=xips-project -Dsonar.projectKey=xips-v2'

                }
            }
        }

        stage('SonarQube Quality Gate check') {
                    steps {
                        timeout(time: 5, unit: 'MINUTES') {
                            withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                                withCredentials([string(credentialsId: 'SONAR_HOST_URL', variable: 'SONAR_HOST_URL')]) {
                                    script {
                                        def scanMetadataReportFile = readFile('target/sonar/report-task.txt').trim()
                                        sh """
                                            curl -sSfL https://raw.githubusercontent.com/SonarSource/sonarqube-quality-gate-action/master/sonarqube.sh | bash -s -- \
                                            -t ${SONAR_TOKEN} \
                                            -u ${SONAR_HOST_URL} \
                                            -f ${scanMetadataReportFile}
                                        """
                                    }
                                }
                            }
                        }
                    }
                }



        stage('Retrieve version') {
            steps {
                script {
                    version = sh(script: '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                    writeFile file: "${env.WORKSPACE}/TAG_NAME", text: "TAG_NAME=${version}"
                }
            }
        }

        stage('Remove Docker Context') {
                    steps {
                        sh 'docker context rm -f my-context'
                    }
                }

        stage('Create Docker Context') {
            steps {
                sh 'docker context create my-context'
            }
        }

        stage('Set Docker Context') {
            steps {
               sh 'docker context use my-context'
            }
        }


        stage('Login to Docker Hub') {
            steps {
                withCredentials([
                    string(credentialsId: 'DOCKER_USERNAME', variable: 'DOCKER_USERNAME'),
                    string(credentialsId: 'DOCKER_TOKEN', variable: 'DOCKER_TOKEN')
                ]) {
                    sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_TOKEN}"
                }
            }
        }

        stage('Set up Docker Buildx') {
            steps {
                script {
                    sh 'docker buildx create --use my-context'
                }
            }
        }


        stage('Build and push') {
            steps {
                script {
                    def dockerTag = "${DOCKER_USERNAME}/xips-v2"
                    def latestTag = "${dockerTag}:latest"
                    def versionTag = "${dockerTag}:${version}"
                    sh "docker buildx build --push --tag $latestTag --tag $versionTag ."
                }
            }
        }

    }

     post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                junit 'target/surefire-reports/*.xml'
            }
        }
}