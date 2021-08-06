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
                echo ' ansible'
                sh '''#!/bin/bash
                rm -fr pipeline-test
                git clone https://github.com/catalincostea/pipeline-test.git
                #  git pull
                echo ----
                pwd
                echo ----
                ls -l
                echo ----
                # find
                cat pipeline-test/ping.yaml
                cat pipeline-test/dev.inv
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
                  ansiblePlaybook disableHostKeyChecking: true, becomeUser: 'admin', credentialsId: 'red-dev-admin', installation: 'ansible', playbook: 'pipeline-test/ping.yaml', inventory: 'pipeline-test/dev.inv', sudoUser: null
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
