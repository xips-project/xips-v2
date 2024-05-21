pipeline {
    agent any

    environment {
        DOCKER_USERNAME = credentials('DOCKER_USERNAME')
        DOCKER_TOKEN = credentials('DOCKER_TOKEN')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
    }
    stages {

        stage('Build') {
            steps {
                sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.9.6/bin/mvn verify'
            }
        }



        stage('Sonar Scan') {
            steps {
                withSonarQubeEnv('sonarcloud') {
                    sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.9.6/bin/mvn verify sonar:sonar -Pcoverage -Dsonar.token=99ca41e7cdcf8d690af802b3917bbe26f2c716d8 -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=xips-project -Dsonar.projectKey=xips-v2'
                }
            }
        }

        stage("Quality Gate Check") {
            steps {
                script {
                    def qualityGateStatus = ''
                    def timeoutMinutes = 30

                    echo "Polling SonarQube for Quality Gate status..."
                    timeout(time: timeoutMinutes, unit: 'MINUTES') {
                        while (qualityGateStatus != 'IN_PROGRESS') {
                            qualityGateStatus = sh(script: "curl -s -u ${SONAR_TOKEN}: https://sonarcloud.io/api/qualitygates/project_status?projectKey=xips-v2", returnStdout: true).trim()
                            if (qualityGateStatus.contains('projectStatus":{"status":"OK"')) {
                                echo "Quality Gate passed!"
                                break
                            } else if (qualityGateStatus.contains('projectStatus":{"status":"ERROR"')) {
                                error "Quality Gate failed!"
                            } else {
                                echo "Quality Gate is still in progress. Waiting..."
                            }
                        }
                    }
                }
            }
        }

        stage('PIT Mutation') {
            steps {
               sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.9.6/bin/mvn -DwithHistory org.pitest:pitest-maven:mutationCoverage'
               sh 'ls -l target/pit-reports'
            }
        }



        stage('Retrieve version') {
            steps {
                script {
                    version = sh(script: '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.9.6/bin/mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                    echo "Retrieved version: ${version}"
                    writeFile file: "${env.WORKSPACE}/TAG_NAME", text: "TAG_NAME=${version}"
                }
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

        stage('Build and push') {
            steps {
                script {
                    def dockerTag = "${DOCKER_USERNAME}/xips-v2"
                    def versionTag = "${dockerTag}:${version}"
                    def latestTag = "${dockerTag}:latest"

                    // Build the Docker image
                    sh "docker build --tag ${dockerTag} ."

                    // Tag the image with the latest and version tags
                    sh "docker tag ${dockerTag} ${latestTag}"
                    sh "docker tag ${dockerTag} ${versionTag}"

                    // Push the images to the repository
                    sh "docker push ${latestTag}"
                    sh "docker push ${versionTag}"
                }
            }
        }

    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            junit 'target/surefire-reports/*.xml'
            // Jenkins plugin outdated and not reading xml file properly
            // pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**/target/pit-reports/**/mutations.xml'
        }
    }
}