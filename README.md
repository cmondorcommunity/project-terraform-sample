project-terraform-sample
========================

Sample Project Infrastructure Repo for Toolkit


```
-── aws - Cloud Provider
│   ├── account - Account-wide resources
│   │   └── global - global phase terraform code
│   │   │   ├── provider.tf - terraform and aws provider TF
│   │   │   └── variables.tf - vars used in phase that can be set
│   │   └── global.tfvars - Global global vars, yeah we know
│   ├── region
│   │   ├── compute - Most active folder, TF Code for LBs, services,lambdas etc go here
│   │   │   ├── network.tf - VPC/Network discovery Data Sources
│   │   │   ├── overrides - overrides directory to keep region/env specific data
│   │   │   │   └── us-west-2_dev.tf - overrides for us-west-2 dev environment
│   │   │   ├── provider.tf
│   │   │   └── variables.tf
│   │   ├── global.tfvars - Gloal Region vars
│   │   ├── network - Least active folder, TF Code to build out VPC, subnets and such
│   │   │   ├── overrides
│   │   │   │   └── us-west-2_dev.tf
│   │   │   ├── provider.tf
│   │   │   ├── variables.tf
│   │   │   └── vpc.tf
│   │   └── storage - Lesser active folder, used for persistance-based resources S3, RDS, Elasticache etc
│   │       ├── network.tf
│   │       ├── overrides
│   │       │   └── us-west-2_dev.tf
│   │       ├── provider.tf
│   │       └── variables.tf
│   └── super_global.tfvars - Super Globals
├── LICENSE
├── pipelines - Where Automated Workflows go
│   ├── Jenkinsfile.aws.packer.groovy - Builds AMIs using packer
│   └── Jenkinsfile.aws.terraform.groovy - Where the magic happens
├── README.md
└── scripts
    └── terraform_init.sh - Terraform Initializer Script, see Jenkinsfiles

 ```


TODO:
- terraform pipeline
- packer pipeline
- packer repo
