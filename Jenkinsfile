pipeline {
    agent any
    environment {
        DOCKER_USERNAME = credentials('DOCKER_USERNAME')
        DOCKER_TOKEN = credentials('DOCKER_TOKEN')
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
                sh 'mvn verify sonar:sonar -Pcoverage -Dsonar.token=99ca41e7cdcf8d690af802b3917bbe26f2c716d8 -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=xips-project -Dsonar.projectKey=xips-v2'
            }
        }
        stage('SonarQube Quality Gate check') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        def scanMetadataReportFile = readFile('target/sonar/report-task.txt').trim()
                        env.SONAR_TOKEN = '99ca41e7cdcf8d690af802b3917bbe26f2c716d8'
                        env.SONAR_HOST_URL = 'https://sonarcloud.io'
                        withCredentials([usernamePassword(credentialsId: 'SONAR_TOKEN', passwordVariable: 'SONAR_TOKEN', usernameVariable: '')]) {
                            sh "export SONAR_TOKEN=${env.SONAR_TOKEN}"
                        }
                        withCredentials([usernamePassword(credentialsId: 'SONAR_HOST_URL', passwordVariable: 'SONAR_HOST_URL', usernameVariable: '')]) {
                            sh "export SONAR_HOST_URL=${env.SONAR_HOST_URL}"
                        }
                        sh "curl -sSfL https://raw.githubusercontent.com/SonarSource/sonarqube-quality-gate-action/master/sonarqube.sh | bash -s -- -t $SONAR_TOKEN -u $SONAR_HOST_URL -f $scanMetadataReportFile"
                    }
                }
            }
        }
        stage('Retrieve version') {
            steps {
                script {
                    version = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                    writeFile file: "${env.WORKSPACE}/TAG_NAME", text: "TAG_NAME=${version}"
                }
            }
        }
        stage('Login to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKER_TOKEN', passwordVariable: 'DOCKER_TOKEN', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_TOKEN"
                    }
                }
            }
        }
        stage('Set up Docker Buildx') {
            steps {
                script {
                    sh 'docker buildx create --use'
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
}
