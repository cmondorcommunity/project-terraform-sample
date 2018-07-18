#!/bin/env bash

function usage {
	echo "USAGE: $0 environment region"
	echo "environment: [dev|prod]"
    echo "region: [us-west-2|us-east-1]"
}

# ensure cleanroom
if [ -d .terraform ]; then
	rm -rf .terraform
fi

if [ $# -lt 1 ]; then
	usage
	exit 1
fi

# https://www.terraform.io/docs/configuration/override.html
OVERRIDES_DIR=overrides
OVERRIDES_PATH="${OVERRIDES_DIR}/$1_$2.tf" #toolkitconvetion
[ -e override.tf ] && rm override.tf
[ -d ${OVERRIDES_DIR} ] && {
    [ -e ${OVERRIDES_PATH} ] && {
        echo "Linking overrides:  ./${OVERRIDES_PATH} -> ./override.tf"
        ln -s ${OVERRIDES_PATH} override.tf
    }
}


GLOBALS_PATH="../global.tfvars" #toolkitconvetion
[ -e global.auto.tfvars ] && rm global.auto.tfvars
[ -e ../../${GLOBALS_PATH} ] && {
        echo "Linking Globals:  ${GLOBALS_PATH} -> ./global.auto.tfvars"
        ln -s ${OVERRIDES_PATH} global.auto.tfvars
    }
}

SUPER_GLOBALS_PATH="../../super_global.tfvars" #toolkitconvetion
[ -e super_global.auto.tfvars ] && rm super_global.auto.tfvars
[ -e ../../${SUPER_GLOBALS_PATH} ] && {
        echo "Linking Super Globals:  ${SUPER_GLOBALS_PATH} -> ./global.auto.tfvars"
        ln -s ${SUPER_GLOBALS_PATH} super_global.auto.tfvars
    }
}

PHASE=$(basename $(pwd))
CLOUD=$(basename $(dirname $(dirname $(pwd))))

terraform init -input=false -backend-config="key=\"${1}-${PHASE}-${2}-${CLOUD}.tfstate\""
terraform validate
