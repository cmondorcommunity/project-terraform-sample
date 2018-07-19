#!/usr/bin/env bash

function usage {
	echo "USAGE: $0 environment region statebucket"
	echo "environment: [dev|prod]"
    echo "region: [us-west-2|us-east-1]"
    echo "statebucket: org-tlkt-tfstate" # ensure matches
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
OVERRIDES_PATH="${OVERRIDES_DIR}/$2_$1.tf" #toolkitconvetion
[ -f override.tf ] && rm override.tf
[ -d ${OVERRIDES_DIR} ] && {
    [ -f ${OVERRIDES_PATH} ] && {
        echo "Linking overrides:  ./${OVERRIDES_PATH} -> ./override.tf"
        ln -s ${OVERRIDES_PATH} override.tf
    }
}

GLOBALS_PATH="../global.tfvars" #toolkitconvetion
[ -f global.auto.tfvars ] && rm global.auto.tfvars
[ -f ${GLOBALS_PATH} ] && {
    echo "Linking Globals:  ${GLOBALS_PATH} -> ./global.auto.tfvars"
    ln -s ${GLOBALS_PATH} global.auto.tfvars
}

SUPER_GLOBALS_PATH="../../super_global.tfvars" #toolkitconvetion
[ -f super_global.auto.tfvars ] && rm super_global.auto.tfvars
[ -f ${SUPER_GLOBALS_PATH} ] && {
    echo "Linking Super Globals:  ${SUPER_GLOBALS_PATH} -> ./global.auto.tfvars"
    ln -s ${SUPER_GLOBALS_PATH} super_global.auto.tfvars
}

PHASE=$(basename $(pwd))
CLOUD=$(basename $(dirname $(dirname $(pwd))))

terraform init -input=false -backend-config="region=\"${2}\"" -backend-config="key=\"${1}-${PHASE}-${2}-${CLOUD}.tfstate\"" -backend-config="bucket=\"${3}\""

terraform validate
