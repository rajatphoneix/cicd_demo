pipeline {
    agent any
    environment {
        IMAGE_NAME = "rajaraok2109/patient-service"
        IMAGE_TAG = "${BUILD_NUMBER}"
        ECR_REPO = "390844763711.dkr.ecr.ap-south-1.amazonaws.com/patient-service"
    }
    stages {
        stage('Checkout') {
            steps { git 'https://bitbucket.org/raja_rao_k/cicd_repo.git' }
        }
        stage('Unit Tests') {
            steps { sh 'mvn clean test' }
        }
        stage('Build Artifact') {
            steps { sh 'mvn clean package -DskipTests' }
        }
        stage('Docker Build') {
            steps { sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .' }
        }
        stage('Docker Push to ECR') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
                    sh '''
                    aws ecr get-login-password | docker login --username AWS --password-stdin $ECR_REPO
                    docker tag $IMAGE_NAME:$IMAGE_TAG $ECR_REPO:$IMAGE_TAG
                    docker push $ECR_REPO:$IMAGE_TAG
                    '''
                }
            }
        }
        stage('Update Manifests') {
            steps {
                sh '''
                sed -i "s|image: .*|image: $ECR_REPO:$IMAGE_TAG|" k8s/deployment.yaml
                git config user.email "raja.netjava@gmail.com"
                git config user.name "Raja Java"
                git commit -am "Update image to $IMAGE_TAG"
                git push
                '''
            }
        }
    }
}
