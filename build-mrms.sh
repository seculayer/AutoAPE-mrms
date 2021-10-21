#!/bin/bash


VERSION="1.0.0"

MAVEN_BUILD=${1}
REGISTRY_URL="registry.seculayer.com:31500"

# maven build

# docker build
# DOCKER_BUILDKIT=1 docker build -t registry.seculayer.com:31500/ape/automl-mrms:1.0.0 .
DOCKER_BUILDKIT=1 docker build -t $REGISTRY_URL/ape/automl-mrms:$VERSION .
docker push $REGISTRY_URL/ape/automl-mrms:$VERSION
