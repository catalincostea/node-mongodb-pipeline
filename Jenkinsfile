def gv

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
                echo ' ansible'
                sh '''#!/bin/bash
                rm -fr pipeline-test
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
                    def HOST_IP = sh(script: "grep ansible_user ansible/inv/dev/hosts | head -n 1 | grep -v '^#' | awk '{ print \$1 }'", returnStdout: true).trim()
                    final String url = "http://$HOST_IP/item/list"
                    sleep(time:5, unit:"SECONDS")

                    final String response = sh(script: "curl -s $url", returnStdout: true).trim()
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
                  ansiblePlaybook playbook: 'ansible/ping.yaml', inventory: 'ansible/inv/prod/hosts', becomeUser: 'admin', credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true
                  //  limit: 'prod',
            }
        }
        stage("validate") {

            steps {
                script {
                    gv.validateApp()
                }
                script {
                    def HOST_IP = sh(script: "grep ansible_user ansible/inv/prod/hosts | head -n 1 | grep -v '^#' | awk '{ print \$1 }'", returnStdout: true).trim()
                    final String url = "http://$HOST_IP/item/list"
                    sleep(time:5, unit:"SECONDS")

                    final String response = sh(script: "curl -s $url", returnStdout: true).trim()
                    // final String response = sh(script: "curl -s `grep ansible_user ansible/inv/dev/hosts | grep -v '^#' | awk '{ print \$1 }'`", returnStdout: true).trim()
                    echo response
                }
            }
        }
    }   
}
