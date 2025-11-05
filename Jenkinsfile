pipeline {
    agent any

    environment {
        APP_NAME = "company-webapp"
        DOCKERHUB_USER = "your-dockerhub-username"
        DOCKERHUB_CREDENTIALS = "dockerhub-creds"  // Jenkins credential ID
        KUBECONFIG_CREDENTIALS = "kubeconfig"     // Jenkins credential ID for kubeconfig
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/yourusername/company-webapp.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh """
                        docker build -t ${DOCKERHUB_USER}/${APP_NAME}:latest .
                    """
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}", usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                        sh """
                            echo "$PASS" | docker login -u "$USER" --password-stdin
                            docker push ${DOCKERHUB_USER}/${APP_NAME}:latest
                        """
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: "${KUBECONFIG_CREDENTIALS}", variable: 'KUBECONFIG')]) {
                        sh """
                            kubectl --kubeconfig=$KUBECONFIG apply -f deployment.yaml
                            kubectl --kubeconfig=$KUBECONFIG rollout status deployment/${APP_NAME}-deployment
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment Successful!"
        }
        failure {
            echo "❌ Build or Deployment Failed!"
        }
    }
}
