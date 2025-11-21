pipeline {
    agent any

    environment {
        // Repository & Branch
        GIT_REPO_URL = 'https://github.com/enreap/maven-web-app1'
        BRANCH_NAME  = 'main'

        // Tool names as configured in Jenkins
        SONARQUBE_ENV = 'sonar-scanner'  // Manage Jenkins → Configure System → SonarQube servers
        MAVEN_HOME    = tool 'maven'     // Global Tool Configuration
        JAVA_HOME     = tool 'jdk-21'    // Global Tool Configuration → JDK
        PATH          = "${JAVA_HOME}/bin:${PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Cloning code from ${GIT_REPO_URL} (branch: ${BRANCH_NAME})"
                git branch: "${BRANCH_NAME}", url: "${GIT_REPO_URL}"
            }
        }

        stage('Build with Maven') {
            steps {
                echo "Building the project using JDK 21..."
                sh "${MAVEN_HOME}/bin/mvn clean install -DskipTests"
            }
        }
        stage("Sonarqube Analysis "){
            steps{
                withSonarQubeEnv('sonar-server') {
                    sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=maven-web-app1 \
                    -Dsonar.java.binaries=. \
                    -Dsonar.projectKey=Petclinic '''
    
                }
            }
        }		

        // stage('SonarQube Analysis') {
        //     steps {
        //         echo "Running SonarQube static code analysis..."
        //         withSonarQubeEnv("${SONARQUBE_ENV}") {
        //             withCredentials([string(credentialsId: 'sonartoken1', variable: 'SONAR_TOKEN')]) {
        //                 sh """
        //                     ${MAVEN_HOME}/bin/mvn clean verify sonar:sonar \\
								// -Dsonar.projectKey=enreapdevops_sonar-java-demo_59ee0fd6-b269-4ef0-9631-04a882dbb2e4 \
								// -Dsonar.projectName='sonar-java-demo' \\
								// -Dsonar.host.url=http://sonarqube-alb-1691354020.us-east-1.elb.amazonaws.com \\
								// -Dsonar.token=sqp_aefb6fec5eb30179b0b435dcda997cebe66371e5\\
								// -Dsonar.qualitygate.wait=true
        //                 """
        //             }
        //         }
        //     }
        // }

        stage('Quality Gate') {
            steps {
                script {
                    echo "Waiting for SonarQube Quality Gate..."
                    timeout(time: 5, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning workspace...'
            cleanWs()
        }
        success {
            echo 'SonarQube analysis completed successfully!'
        }
        failure {
            echo 'Build or analysis failed — check Jenkins and SonarQube logs.'
        }
    }
}

// pipeline {
//     agent any

//     environment {
//         APP_NAME = "maven-web-app"
//         DOCKERHUB_USER = "-dockerhub-username"
//         DOCKERHUB_CREDENTIALS = "dockerhub-creds"  // Jenkins credential ID
//         KUBECONFIG_CREDENTIALS = "kubeconfig"     // Jenkins credential ID for kubeconfig
//     }

//     stages {
//         stage('Clone Repository') {
//             steps {
//                 git branch: 'main', url: 'git@github.com:enreapdevops/maven-web-app.git'
//             }
//         }

//         stage('Maven Build') {
//             steps {
//                 sh 'mvn clean package -DskipTests'
//             }
//             post {
//                 success {
//                     archiveArtifacts artifacts: 'target/*.war', fingerprint: true
//                 }
//             }
//         }
//         stage('SonarQube Scan') {
//             steps {
//                 echo " Running SonarQube Analysis..."
//                 withSonarQubeEnv('SonarQubeServer') {
//                     withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
//                         sh """
//                             mvn sonar:sonar \
//                               -Dsonar.projectKey=${APP_NAME} \
//                               -Dsonar.host.url=${SONAR_HOST_URL} \
//                               -Dsonar.login=$SONAR_TOKEN
//                         """
//                     }
//                 }
//             }
//         }
//         stage('Push Artifact to Nexus') {
//             steps {
//                 echo " Deploying artifact to Nexus..."
//                 withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
//                     sh """
//                         mkdir -p ~/.m2
//                         cat > ~/.m2/settings.xml <<EOF
// <settings>
//   <servers>
//     <server>
//       <id>nexus</id>
//       <username>$NEXUS_USER</username>
//       <password>$NEXUS_PASS</password>
//     </server>
//   </servers>
// </settings>
// EOF
//                         mvn clean deploy -DskipTests
//                     """
//                 }
//             }
//         }
            
//         stage('Docker Build') {
//             steps {
//                 script {
//                     sh """
//                         docker build -t ${DOCKERHUB_USER}/${APP_NAME}:latest .
//                     """
//                 }
//             }
//         }

//         stage('Docker Push') {
//             steps {
//                 script {
//                     withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}", usernameVariable: 'USER', passwordVariable: 'PASS')]) {
//                         sh """
//                             echo "$PASS" | docker login -u "$USER" --password-stdin
//                             docker push ${DOCKERHUB_USER}/${APP_NAME}:latest
//                         """
//                     }
//                 }
//             }
//         }

//         stage('Deploy to Kubernetes') {
//             steps {
//                 script {
//                     withCredentials([file(credentialsId: "${KUBECONFIG_CREDENTIALS}", variable: 'KUBECONFIG')]) {
//                         sh """
//                             kubectl --kubeconfig=$KUBECONFIG apply -f deployment.yaml
//                             kubectl --kubeconfig=$KUBECONFIG rollout status deployment/${APP_NAME}-deployment
//                         """
//                     }
//                 }
//             }
//         }
//     }

//     post {
//         success {
//             echo "Deployment Successful!"
//         }
//         failure {
//             echo "Build or Deployment Failed!"
//         }
//     }
// }
