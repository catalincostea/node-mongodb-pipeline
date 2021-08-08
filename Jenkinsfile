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
                // ansiblePlaybook(
                //     playbook: 'ansible/ping.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin', 
                //     credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, 
                //     disableHostKeyChecking: true, 
                //     vaultCredentialsId: 'wr_token', extras: 'wr_token=cf6ee0a0-84ca-488f-8217-70e22a3e9a04'
                // )

                script {
                   gv = load "script.groovy" 
                }
                echo ' ansible'
                sh '''#!/bin/bash
                rm -fr pipeline-test
                find
                set
                '''
            }
        }
        stage("build") {
            steps {
                //ansiblePlaybook playbook: 'ansible/build.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin', credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true
                ansiblePlaybook(
                    playbook: 'ansible/build.yaml', inventory: 'ansible/inv/dev/hosts', becomeUser: 'admin',
                    credentialsId: 'red-dev-admin', installation: 'ansible', sudoUser: null, disableHostKeyChecking: true,
                    vaultCredentialsId: 'wr_token', extras: 'wr_token=cf6ee0a0-84ca-488f-8217-70e22a3e9a04'
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
