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

        stage('Unit Tests') {
            steps {
                sh 'mvn clean test'
            }
        }

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

        stage('Docker Push to ECR') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
                    sh '''
                        aws ecr get-login-password --region ap-south-1 | \
                        docker login --username AWS --password-stdin 390844763711.dkr.ecr.ap-south-1.amazonaws.com

                        docker tag $IMAGE_NAME:$IMAGE_TAG $ECR_REPO:$IMAGE_TAG
                        docker push $ECR_REPO:$IMAGE_TAG
                    '''
                }
            }
        }
    }
}
