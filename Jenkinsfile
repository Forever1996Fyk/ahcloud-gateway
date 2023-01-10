pipeline {
  agent {
    node {
      label 'maven'
    }

  }
  stages {
    stage('拉取代码') {
      agent none
      steps {
        container('base') {
          git(url: 'http://172.31.0.164/ahcloud/ahcloud-gateway.git', credentialsId: 'ahcloud-gateway-deploy', branch: 'dev', changelog: true, poll: false)
          sh 'll'
        }

      }
    }

    stage('Run compile') {
      steps {
        container('maven') {
          sh 'mvn compile'
        }

      }
    }

    stage('Run test') {
      steps {
        container('maven') {
          sh 'mvn clean test'
        }

      }
    }

    stage('Run build') {
      steps {
        container('maven') {
          sh 'mvn package'
        }

      }
    }

    stage('Archive artifacts') {
      steps {
        archiveArtifacts 'target/*.jar'
      }
    }

  }
}