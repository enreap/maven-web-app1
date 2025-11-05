pipeline {
    agent any

    environment {
        APP_NAME = "maven-web-app"
        DOCKERHUB_USER = "-dockerhub-username"
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
        stage('SonarQube Scan') {
            steps {
                echo " Running SonarQube Analysis..."
                withSonarQubeEnv('SonarQubeServer') {
                    withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                        sh """
                            mvn sonar:sonar \
                              -Dsonar.projectKey=${APP_NAME} \
                              -Dsonar.host.url=${SONAR_HOST_URL} \
                              -Dsonar.login=$SONAR_TOKEN
                        """
                    }
                }
            }
        }
        stage('Push Artifact to Nexus') {
            steps {
                echo " Deploying artifact to Nexus..."
                withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh """
                        mkdir -p ~/.m2
                        cat > ~/.m2/settings.xml <<EOF
<settings>
  <servers>
    <server>
      <id>nexus</id>
      <username>$NEXUS_USER</username>
      <password>$NEXUS_PASS</password>
    </server>
  </servers>
</settings>
EOF
                        mvn clean deploy -DskipTests
                    """
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
            echo "Deployment Successful!"
        }
        failure {
            echo "Build or Deployment Failed!"
        }
    }
}
