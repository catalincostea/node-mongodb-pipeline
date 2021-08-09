def gv
def url_dev
def url_prod

pipeline {
    agent any
    parameters {
        //choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
        booleanParam(name: 'executeTests', defaultValue: true, description: '')
    }
    environment {
        wr_token = credentials('wr_token2')
    }
    stages {    
        stage("init code") {
            steps {
                // ansiblePlaybook(
                //     playbook: 'ansible/ping.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin', 
                //     credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, 
                //     disableHostKeyChecking: true 
                // )

                script {
                   gv = load "script.groovy" 
                }
                sh '''#!/bin/bash
                # rm -fr node-mongodb-pipeline
                # find
                # set
                '''
            }
        }
        stage("build") {
            steps {
                ansiblePlaybook( 
                    playbook: 'ansible/build.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin', 
                    credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true
                )
            }
        }
        stage("test") {
            when {
                expression {
                    params.executeTests
                }
            }
            steps {
                script {
                    gv.testApp()
                }
                script {
                    def HOST_IP = sh(script: "grep ansible_user ansible/inv/dev/hosts | grep -v '^#' | head -n 1 | awk '{ print \$1 }'", returnStdout: true).trim()
                    url_dev = HOST_IP
                    final String url = "http://$HOST_IP/item/list"
                    sleep(time:5, unit:"SECONDS")

                    final String response = sh(script: "curl -s $url", returnStdout: true).trim()
                    // Additional tests CRUD operations and DB consistency...
                    // final String response = sh(script: "curl -s `grep ansible_user ansible/inv/dev/hosts | grep -v '^#' | awk '{ print \$1 }'`", returnStdout: true).trim()
                    echo response
                }
            }
        }
        stage("publish") {
            steps {
                ansiblePlaybook(
                    playbook: 'ansible/publish.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin', 
                    credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true, 
                    vaultCredentialsId: 'wr_token', extras: "-e wr_token=${wr_token}"
                )           
            }
        }
        stage("deploy") {
            steps {
                ansiblePlaybook( 
                    playbook: 'ansible/build.yaml', inventory: 'ansible/inv/prod/hosts', becomeUser: 'admin', 
                    credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true
                )
            }
        }
        stage("validate") {

            steps {
                script {
                    gv.validateApp()
                }
                script {
                    def HOST_IP = sh(script: "grep ansible_user ansible/inv/prod/hosts | grep -v '^#' | head -n 1 | awk '{ print \$1 }'", returnStdout: true).trim()
                    url_prod = HOST_IP
                    final String url = "http://$HOST_IP/item/list"
                    sleep(time:5, unit:"SECONDS")

                    final String response = sh(script: "curl -s $url", returnStdout: true).trim()
                    // final String response = sh(script: "curl -s `grep ansible_user ansible/inv/dev/hosts | grep -v '^#' | awk '{ print \$1 }'`", returnStdout: true).trim()
                    echo response
                }
                echo "Dev env: http://$url_dev"
                echo "Prod env: http://$url_prod"
            }
        }
    }   
}
