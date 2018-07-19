variable "cidr" {
  default = "10.0.0.0/16"
}

variable "availability_zones" {
  type    = "list"
  default = ["us-west-2a", "us-west-2b"]
}

variable "private_subnet_cidrs" {
  type    = "list"
  default = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "public_subnet_cidrs" {
  type    = "list"
  default = ["10.0.101.0/24", "10.0.102.0/24"]
}
