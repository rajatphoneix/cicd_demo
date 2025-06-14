pipeline {
    agent any
    tools {
      maven 'Maven_3.8.8' // Must match the name you gave in UI
    }
    environment {
        IMAGE_NAME = "rajatphoneix/cicd_demo"
        IMAGE_TAG = "${BUILD_NUMBER}"
        AWS_REGION = "ap-south-1"
        ECR_REGISTRY = "390844763711.dkr.ecr.ap-south-1.amazonaws.com"
        ECR_REPO = "${ECR_REGISTRY}/cicd_demo_repo"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/rajatphoneix/cicd_demo.git'
            }
        }

        // Optional - uncomment if you want to re-enable tests
       stage('Unit Tests') {
           steps {
               catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                   sh 'mvn -B test -fae'
               }
           }
           post {
               always {
                   junit '**/target/surefire-reports/*.xml'
               }
           }
       }

        stage('Build Artifact') {
            steps {
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Docker Push to ECR') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
                    sh '''
                        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}

                        # Push with build number tag
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${ECR_REPO}:${IMAGE_TAG}
                        docker push ${ECR_REPO}:${IMAGE_TAG}

                        # Check if 'latest' tag exists
                        TAG_EXISTS=$(aws ecr describe-images --repository-name cicd_demo_repo --region ${AWS_REGION} \
                          --query "imageDetails[?contains(imageTags, 'latest')].imageTags" --output text)

                        if [ -z "$TAG_EXISTS" ]; then
                            echo "No 'latest' tag found. Pushing with 'latest' tag..."
                            docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${ECR_REPO}:latest
                            docker push ${ECR_REPO}:latest
                        else
                            echo "'latest' tag already exists."
                        fi
                    '''
                }
            }
        }

        stage('Deploy to ECS') {
           steps {
               withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
                   sh """
                       aws ecs update-service \
                         --cluster springboot-cluster \
                         --service springboot-service \
                         --force-new-deployment \
                         --region ap-south-1
                    """
                        }
                    }
                }


    }
}
