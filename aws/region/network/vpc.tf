module "vpc" {
  source = "terraform-aws-modules/vpc/aws"

  name = "${var.environment}-${var.project}-${var.org}"
  cidr = "${var.cidr}"

  azs                  = ["${var.availability_zones}"]
  private_subnets      = ["${var.private_subnet_cidrs}"]
  public_subnets       = ["${var.public_subnet_cidrs}"]
  enable_dns_hostnames = true

  enable_nat_gateway = true
  enable_vpn_gateway = false

  tags = {
    Terraform   = "true"
    org         = "${var.org}"
    project     = "${var.project}"
    environment = "${var.environment}"
  }

  public_subnet_tags = {
    subnet = "public"
  }

  private_subnet_tags = {
    subnet = "private"
  }
}
