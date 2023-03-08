def call(Map config = [:]){

pipeline {

    options {
        buildDiscarder(logRotator(daysToKeepStr: '1', numToKeepStr: '3'))
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 10, unit: 'MINUTES')
    }

    parameters {
        string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')

        text(name: 'BIOGRAPHY', defaultValue: '', description: 'Enter some information about the person')

        booleanParam(name: 'TOGGLE', defaultValue: true, description: 'Toggle this value')

        choice(name: 'CHOICE', choices: ['One', 'Two', 'Three'], description: 'Pick something')

        password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'Enter a password')
    }

//    agent {
//     docker {
//         image 'bibiefrat/ci_cd_1:docker-slave'
//         args  '-v /var/run/docker.sock:/var/run/docker.sock -u root'
//         }
//     }
    agent any
    environment {
    // get the snyk token from the jenkins general credentials
        //SNYK_TOKEN = credentials('synk_token')
    }

    stages {

//         stage('Code Checkout') {
//                 steps {
//                     checkout([
//                         $class: 'GitSCM',
//                         branches: [[name: '*/main']],
//                         userRemoteConfigs: [[url: 'https://github.com/spring-projects/spring-petclinic.git']]
//                     ])
//                 }
//             }

        stage('Stage 1 ---> Build PolyBot') {
            agent {
                docker {
                    image 'bibiefrat/ci_cd_1:docker-slave'
                    args  '-v /var/run/docker.sock:/var/run/docker.sock -u root'
                }
            }
            steps {
                //withCredentials([usernamePassword(credentialsId: 'docker_hub_ci_cd_repo', passwordVariable: 'pass', usernameVariable: 'user')]) {
                sh """
                echo Stage 1
           """
                }
            }
        }

        stage('Stage 2 parallel ---> - Pylint Unitest and Snyk') {
            parallel {
                stage('Stage 3 ---> testing with Snyk plybot image') {
                    agent {
                            docker {
                                image 'bibiefrat/ci_cd_1:docker-slave'
                                args  '-v /var/run/docker.sock:/var/run/docker.sock -u root'
                            }//docker
                    }//agent
                    steps {
                        script {
                                sh 'echo "parallel 2 ---> stage 3"'
                        }
                    }
                }
                stage('Stage 4 ---> Pylint') {
                //agent any
                    agent {
                        docker {
                            image 'bibiefrat/ci_cd_1:docker-slave'
                            args  '-v /var/run/docker.sock:/var/run/docker.sock -u root'
                        }//docker
                    }//agent
                    steps {
                         script {
                                sh "echo 'parallel 2 --> stage 4'"
                                }//script
                     } //step
                }// stage


                stage('Stage 5 ---> Unitest') {
                    //agent any
                    agent {
                        docker {
                            image 'bibiefrat/ci_cd_1:docker-slave'
                            args  '-v /var/run/docker.sock:/var/run/docker.sock -u root'
                        }// docker
                    }//agent
                    steps {
                         script {
                                sh "echo 'parallel 2 ---> stage 5'"

                               }//script
                    }//steps


                }//stage
            }//parallel
        }//stage

        stage('Stage 6 --->  PolyBot - Push Container') {
            agent {
                    docker {
                        image 'bibiefrat/ci_cd_1:docker-slave'
                        args  '-v /var/run/docker.sock:/var/run/docker.sock -u root'
                    }
                }
            steps {
                    withCredentials([usernamePassword(credentialsId: 'docker_hub_ci_cd_repo', passwordVariable: 'pass', usernameVariable: 'user')]) {
                        sh "echo 'stage 6'"
                    }
            }
        }


    }
    post {
        always {
            sh """
            echo " WE ARE IN POST"
            """
        }
    }
}//end of pipline

}// end of default_pipeline functio