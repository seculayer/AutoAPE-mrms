#!/bin/bash


VERSION="1.0.0"

MAVEN_BUILD=${1}
REGISTRY_URL="registry.seculayer.com:31500"

# maven build

# docker build
docker build -t $REGISTRY_URL/ape/automl-mrms:$VERSION .
docker push $REGISTRY_URL/ape/automl-mrms:$VERSION
