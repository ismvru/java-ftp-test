pipeline {
  agent any
  stages {
    stage('Lint all') {
      parallel {
        stage('docker') {
          agent {
            docker {
              image 'repo.ismv.ru/hadolint:latest'
            }

          }
          steps {
            sh 'lint'
          }
        }

        stage('yaml') {
          agent {
            docker {
              image 'repo.ismv.ru/yamllint:latest'
            }

          }
          steps {
            sh 'lint'
          }
        }

        stage('markdown') {
          agent {
            docker {
              image 'repo.ismv.ru/mdlint:latest'
            }

          }
          steps {
            sh 'lint'
          }
        }

        stage('java') {
          agent {
            docker {
              image 'repo.ismv.ru/javalint:latest'
            }

          }
          steps {
            sh 'lint'
          }
        }

      }
    }

    stage('Java 8') {
      agent {
        docker {
          image 'maven:3-openjdk-8'
        }

      }
      steps {
        sh 'mvn -f pom-1.8.xml clean install -B'
      }
    }

    stage('Get jars - Java 8') {
      steps {
        archiveArtifacts 'target/*.jar'
      }
    }

    stage('Java 11') {
      agent {
        docker {
          image 'maven:3-openjdk-11'
        }

      }
      steps {
        sh 'mvn -f pom-11.xml install -B'
      }
    }

    stage('Get jars - Java 11') {
      steps {
        archiveArtifacts 'target/*.jar'
      }
    }

    stage('Java 17') {
      agent {
        docker {
          image 'maven:3-openjdk-17'
        }

      }
      steps {
        sh 'mvn -f pom-17.xml clean install -B'
      }
    }

    stage('Get jars - Java 17') {
      steps {
        archiveArtifacts 'target/*.jar'
      }
    }

    stage('Send tg message') {
      steps {
        telegramSend(message: '#Jenkins #Finish Job ${env.JOB_NAME} Branch ${env.BRANCH_NAME} Build ${env.BUILD_ID}  ${env.BUILD_URL}', chatId: 658368779)
      }
    }

  }
}