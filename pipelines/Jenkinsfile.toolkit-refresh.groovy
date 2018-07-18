node {
    stage('refresh'){
        sh "aws ec2 terminate-instances --region us-west-2 --instance-ids \$(curl -s http://169.254.169.254/latest/meta-data/instance-id)"
    }
}
