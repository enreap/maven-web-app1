pipeline
{
  agent any
  
  tools
  {
    maven 'Maven_3.8.2'
  }
  
  triggers
  {
    pollSCM('* * * * *')
  }
  
  options
  {
    timestamps()
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '5', daysToKeepStr: '', numToKeepStr: '5'))
  }
  
  stages
  {
    stage('Checkout Code from GitHub')
    {
      steps()
      {
        git branch: 'development', credentialsId: '957b543e-6f77-4cef-9aec-82e9b0230975', url: 'https://github.com/xxxxxxxx/maven-web-application-1.git'
      }
    }
    
    stage('Build Project')
    {
      steps()
      {
        sh "mvn clean package"
      }
    }
    
    stage('Execute SonarQube Report')
    {
      steps()
      {
        sh "mvn clean sonar:sonar"
      }
    }
    
    stage('Upload Artifacts to Sonatype Nexus')
    {
      steps()
      {
        sh "mvn clean deploy"
      }
    }
        
  }
}
