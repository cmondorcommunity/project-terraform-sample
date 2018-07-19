variable "environment" {
  default = "cmo"
}

variable "region" {
  default = "us-west-2"
}

variable "cidr" {
  default = "192.168.0.0/16"
}

variable "availability_zones" {
  type    = "list"
  default = ["us-west-2a", "us-west-2b"]
}

variable "private_subnet_cidrs" {
  type    = "list"
  default = ["192.168.1.0/24", "192.168.2.0/24"]
}

variable "public_subnet_cidrs" {
  type    = "list"
  default = ["192.168.101.0/24", "192.168.102.0/24"]
}
