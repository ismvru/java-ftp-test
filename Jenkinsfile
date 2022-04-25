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

    stage('Build JARs') {
      parallel {
        stage('Java8') {
          agent {
            docker {
              image 'maven:3-openjdk-8'
            }

          }
          steps {
            sh 'mvn -f pom-1.8.xml install -B'
          }
        }

        stage('java11') {
          agent {
            docker {
              image 'maven:3-openjdk-11'
            }

          }
          steps {
            sh 'mvn -f pom-11.xml install -B'
          }
        }

        stage('java17') {
          agent {
            docker {
              image 'maven:3-openjdk-17'
            }

          }
          steps {
            sh 'mvn -f pom-17.xml install -B'
          }
        }

      }
    }

  }
}