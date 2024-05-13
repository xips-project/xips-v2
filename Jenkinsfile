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

        stage('Build') {
            steps {
                sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn clean'
            }
        }


        stage('Sonar Scan') {
            steps {
                withSonarQubeEnv('sonarcloud') {
                    sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn verify sonar:sonar -Pcoverage -Dsonar.token=99ca41e7cdcf8d690af802b3917bbe26f2c716d8 -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=xips-project -Dsonar.projectKey=xips-v2'
                }
            }
        }

        stage("Quality Gate Check") {
            steps {
                script {
                    def qualityGateStatus = ''
                    def timeoutMinutes = 30 // Adjust the timeout as needed

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
                        sh '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn org.pitest:pitest-maven:mutationCoverage'
                        sh 'ls -l target/pit-reports' // Add this line
                        pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**/target/pit-reports/**/mutations.xml'
                    }
                }



        stage('Retrieve version') {
            steps {
                script {
                    version = sh(script: '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven/bin/mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                    echo "Retrieved version: ${version}"
                    writeFile file: "${env.WORKSPACE}/TAG_NAME", text: "TAG_NAME=${version}"
                }
            }
        }

        stage('Setup Docker Context') {
            steps {
                script {
                    def contextExists = sh(script: 'docker context inspect my-context >/dev/null 2>&1', returnStatus: true)
                    if (contextExists != 0) {
                        sh 'docker context create my-context'

                    }
                    sh 'docker context use my-context'
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
                    def versionTag = "${dockerTag}:${version}"
                    def branchName = env.BRANCH_NAME // Get the name of the current branch
                    def prNumber = env.CHANGE_ID // Get the pull request number

                    // Include branch name or pull request number in the image tag
                    if (prNumber != null && prNumber != '') {
                        dockerTag += "-PR-${prNumber}"
                    } else {
                        dockerTag += "-${branchName}"
                    }

                    def latestTag = "${dockerTag}:latest"
                    sh "docker buildx build --push --tag ${latestTag} --tag ${versionTag} ."
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            junit 'target/surefire-reports/*.xml'
            // Not creating reports correctly
            pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**/target/pit-reports/**/mutations.xml'
        }
    }
}
