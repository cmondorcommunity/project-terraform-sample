properties([
        pipelineTriggers([
                githubPush(),
                pollSCM('')
        ])
])

def REVISION = ""
def ENVIRONMENT = "dev"
def AWS_REGION = "us-west-2"
def TF_INIT = "../../../scripts/terraform_init.sh"
def DATESTAMP_CMD = 'TZ="America/Los_Angeles" date +%Y%m%d-%H%M-%a'

def REPO_MAPPING = [:]
REPO_MAPPING['dev'] = '*/develop'
REPO_MAPPING['prod'] = '*/prod'

ansiColor('xterm') {
    node {
        stage('Clone') {
            ENVIRONMENT = env.JOB_NAME.split("-")[0]
            def branchName = (REPO_MAPPING.containsKey(ENVIRONMENT)) ? REPO_MAPPING[ENVIRONMENT] : "*/${ENVIRONMENT}"
            checkout([
                    $class           : 'GitSCM',
                    branches         : [[name: branchName]],
                    poll             : true,
                    extensions       : [],
                    userRemoteConfigs: [[url: 'https://github.com/cmondorcommunity/project-terraform-sample.git']]
            ])
            REVISION = sh(returnStdout: true, script: "git rev-list --max-count=1 HEAD").trim()
        }

        lock('terraform') {
            sh "tfenv install latest"
            sh "tfenv use latest"

            stage('Network Plan') {
                dir("aws/region/network") {
                    sh "ls -l ../../../scripts"
                    sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION}"
                    sh "terraform plan "
                }
            }

            stage('Network Apply') {
                dir("aws/region/network") {
                    input "Apply?"
                    sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION}"
                    sh "terraform apply -auto-approve "
                }
            }

            stage('Storage Plan') {
                dir("aws/region/storage") {
                    sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION}"
                    sh "terraform plan "
                }
            }
            stage('Storage Apply') {
                dir("aws/region/storage") {
                    input "Apply?"
                    sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION}"
                    sh "terraform apply -auto-approve "
                }
            }
            stage('Services Plan') {
                dir("aws/region/compute") {
                    sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION}"
                    sh "terraform plan "
                }
            }
            stage('Services Apply') {
                dir("aws/region/compute") {
                    input "Apply?"
                    sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION}"
                    sh "terraform apply -auto-approve "

                }
            }
            stage('Promote') {
                switch (ENVIRONMENT) {
                    case "dev":
                        input "Push to Prod?"
                        def DATESTAMP = sh(returnStdout: true, script: $ { DATESTAMP_CMD }).trim()
                        sh "git checkout -b ${BUILD_NUMBER} ${REVISION}"
                        sh "git tag -m 'Release #${BUILD_NUMBER} ${env.BUILD_URL}' ${DATESTAMP}"
                        sh "git push --tags origin ${BUILD_NUMBER}:prod"
                        break
                    case "prod":
                        input "Merge/Tag master?"
                        def DATESTAMP = sh(returnStdout: true, script: $ { DATESTAMP_CMD }).trim()
                        sh "git checkout -b ${BUILD_NUMBER} ${REVISION}"
                        sh "git tag -m 'Release  #${BUILD_NUMBER} ${env.BUILD_URL}' ${DATESTAMP}"
                        sh "git push --tags origin ${BUILD_NUMBER}:master"
                        break
                    default:
                        break
                }
            }
        }
    }
}
