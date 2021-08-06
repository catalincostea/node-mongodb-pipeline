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
                script {
                   gv = load "script.groovy" 
                }
                echo ' ansible'
                sh '''#!/bin/bash
                echo ----
                pwd
                echo ----
                ls -l
                echo ----
                # find
                #cat pipeline-test/ping.yaml
                #cat pipeline-test/dev.inv
                docker ps -a
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
                  ansiblePlaybook playbook: 'ansible/ping.yaml', inventory: 'ansible/hosts', becomeUser: 'admin', credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true
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
