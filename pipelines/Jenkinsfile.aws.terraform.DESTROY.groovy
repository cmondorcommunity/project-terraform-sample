properties([
        pipelineTriggers([
                githubPush(),
                pollSCM('')
        ])
])

def ORG = ""
def STATEFILE_BUCKET = ""
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
            if (REPO_MAPPING.containsKey(ENVIRONMENT)) {
                echo "Not intended to destroy shared environments.\nAre you sure you'd like to proceed?"
                input "Destroy Environment: ${ENVIRONMENT}?"
            }

        }

        lock('terraform') {
            sh "tfenv install latest"
            sh "tfenv use latest"
            def command = "grep org aws/super_global.tfvars | cut -d '\"' -f 2"  //ensure matches toolkit/.env
            ORG = sh(returnStdout: true, script: command).trim()
            STATEFILE_BUCKET = "${ORG}-tlkt-tfstate" //ensure matches terraform init backend bucket in scripts/terraform_init.sh
            try {
                stage('Services Plan') {
                    dir("aws/region/compute") {
                        sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION} ${STATEFILE_BUCKET}"
                        sh "terraform plan -destroy"
                    }
                }
                stage('Services Destroy') {
                    dir("aws/region/compute") {
                        input "Destroy?"
                        sh "terraform destroy -force"
                    }
                }
            } catch (exception) {}

            try{
                stage('Storage Plan') {
                    dir("aws/region/storage") {
                        sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION} ${STATEFILE_BUCKET}"
                        sh "terraform plan -destroy "
                    }
                }
                stage('Storage Destroy') {
                    dir("aws/region/storage") {
                        input "Destroy?"
                        sh "terraform destroy -force "
                    }
                }
            } catch (exception) {}

            try {
                stage('Network Plan') {
                    dir("aws/region/network") {
                        sh "ls -l ../../../scripts"
                        sh "../../../scripts/terraform_init.sh ${ENVIRONMENT} ${AWS_REGION} ${STATEFILE_BUCKET}"
                        sh "terraform plan -destroy"
                    }
                }

                stage('Network Destroy') {
                    dir("aws/region/network") {
                        input "Destroy?"
                        sh "terraform destroy -force "
                    }
                }
            } catch (exception) {}
        }
    }
}
