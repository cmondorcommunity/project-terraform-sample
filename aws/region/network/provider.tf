terraform {
  required_version = "~>0.11.7"

  backend "s3" {}
}

provider "aws" {
  region  = "${var.region}"
  profile = "${var.aws_profile}"
}

variable "aws_profile" {
  default = "default"
}
