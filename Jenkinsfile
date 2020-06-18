import hudson.model.*
    
pipeline {
    agent any
    environment {
        dockerImage = ""
        registry = "mscarceller/mcapracticacd"
        registryCredential = 'dockerhub_mscarceller'
        FULL_PATH_BRANCH = "${sh(script:'git name-rev --name-only HEAD', returnStdout: true)}" 
        GIT_BRANCH = FULL_PATH_BRANCH.substring(FULL_PATH_BRANCH.lastIndexOf('/') + 1, FULL_PATH_BRANCH.length()-1)
        GIT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    }
    stages {
        stage('Build image') {
            steps {
                script{
                    dockerImage = docker.build(env.registry)
                }
            }
        }
        stage("Start app") {
            steps {
                sh "docker-compose up -d"
            }
        }
        stage("UnitaryTests and RestTests"){
           steps { 
                sleep 20
                sh 'mvn test -Dtest="RestTest.java, UnitaryTest.java"'
            }
        }
        stage("e2e Selenium Tetst"){
          when { branch "release/*" }
            steps {
                script{
                    sh 'mvn test -Dtest="SeleniumTest.java"'
                    env.IS_RELEASE = "true"
                }
            }
        }
    }
    post {
         always {
             
            sh "docker-compose logs > all-logs.txt"
            archiveArtifacts  "all-logs.txt"

            sh "docker-compose logs web > web-logs.txt"
            archiveArtifacts  "web-logs.txt"

            sh "docker-compose logs db > db-logs.txt"
            archiveArtifacts  "db-logs.txt"

            sh "docker-compose down"
             
            archiveArtifacts "cloudformation.yaml"
            
        }
        success {
            script {
                script {
                    if (env.IS_RELEASE){
                        docker.withRegistry( '', registryCredential ) {
                                dockerImage.push("${GIT_BRANCH}")
                        }
                        sh "docker rmi $registry:${GIT_BRANCH}"
                        
                        def cloudformation_file = new File("${WORKSPACE}/cloudformation.yaml")
                        build(job: "DeployAWS", parameters: [string(name: "DockerImage", value: "$registry:${GIT_BRANCH}"), string(name: "ServiceName", value: "build_${GIT_COMMIT}"),new FileParameterValue('cloudformation.yaml', cloudformation_file, 'original_cloudformation')]) 
                        
                        def subject = ""
                        def bodyText = ""
                        subject = "Release  ${GIT_BRANCH}  for Practica1-CD"
                        bodyText = """
                            Hi there!! 
                            
                            A new release has been published succesfully
                            
                            """
                        emailext body: bodyText, 
                        subject: subject,
                        to: "mscarceller@gmail.com"
                    }
                    if (env.GIT_BRANCH == "master"){
                        docker.withRegistry( '', registryCredential ) {
                                dockerImage.push("latest")
                        }
                        sh "docker rmi $registry:latest"
                        
                        def subject = ""
                        def bodyText = ""
                        subject = "Release latest for Practica1-CD"
                        bodyText = """
                            Hi there!! 
                            
                            A latests release has been published succesfully
                            
                            """
                        emailext body: bodyText, 
                        subject: subject,
                        to: "mscarceller@gmail.com"
                    }
                }
            }
        }
    }
}
