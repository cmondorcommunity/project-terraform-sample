project-terraform-sample
========================

Sample Project Infrastructure Repo for Toolkit


```
.
├── aws
│   ├── global - Account-wide Resources
│   │   ├── global.tfvars - global overrides for global
│   │   └── global
│   │       ├── provider.tf
│   │       └── variables.tf
│   ├── region
│   │   ├── compute
│   │   │   ├── network.tf
│   │   │   ├── overrides
│   │   │   │   └── us-west-2_dev.tf
│   │   │   ├── provider.tf
│   │   │   └── variables.tf
│   │   ├── global.tfvars
│   │   ├── network
│   │   │   ├── overrides
│   │   │   │   └── us-west-2_dev.tf
│   │   │   ├── provider.tf
│   │   │   ├── variables.tf
│   │   │   └── vpc.tf
│   │   └── storage
│   │       ├── network.tf
│   │       ├── overrides
│   │       │   └── us-west-2_dev.tf
│   │       ├── provider.tf
│   │       └── variables.tf
│   └── super_global.tfvars
├── LICENSE
├── pipelines
│   ├── Jenkinsfile.aws.packer.groovy
│   └── Jenkinsfile.aws.terraform.groovy
├── project-terraform-sample.iml
├── README.md
└── scripts
    └── terraform_init.sh
```


TODO:
- terraform pipeline
- packer pipeline
- packer repo
