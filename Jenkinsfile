def gv

pipeline {
    agent any
    parameters {
        choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
        booleanParam(name: 'executeTests', defaultValue: true, description: '')
    }
    stages {
        stage("init code") {
            steps {
                ansiblePlaybook playbook: 'ansible/ping.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin', credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true
                ansiblePlaybook playbook: 'ansible/build.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin', credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true

                script {
                   gv = load "script.groovy" 
                }
                echo ' ansible'
                sh '''#!/bin/bash
                rm -fr pipeline-test
                # git clone https://github.com/catalincostea/pipeline-test.git
                #  git pull
                echo ----
                pwd
                echo ----
                ls -l
                echo ----
                # find
                #cat pipeline-test/ping.yaml
                #cat pipeline-test/dev.inv
                cat ansible/ping.yaml
                cat ansible/inv/dev/hosts
                cat ansible/inv/prod/hosts
                '''
            }
        }
        stage("build") {
            steps {
                script {
                    gv.buildApp()
                }
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
            }
        }
    }   
}
