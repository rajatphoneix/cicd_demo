pipeline {
    agent any

    environment {
        IMAGE_NAME = "rajatphoneix/patient-service"
        IMAGE_TAG = "${BUILD_NUMBER}"
        ECR_REPO = "390844763711.dkr.ecr.ap-south-1.amazonaws.com/patient-service"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/rajatphoneix/cicd_demo.git'
            }
        }

        /* stage('Unit Tests') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    sh 'mvn -B clean test'
                }
            }
        } */

        stage('Build Artifact') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

         stage('Push to ECR') {
                    steps {
                        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
                            sh """
                                set -x
                                aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                                docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${ECR_REPO}:${IMAGE_TAG}
                                docker push ${ECR_REPO}:${IMAGE_TAG}
                            """
                        }
                    }
        }
    }
}
