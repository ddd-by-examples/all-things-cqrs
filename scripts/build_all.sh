#!/usr/bin/env bash

source common.sh || source scripts/common.sh || echo "No common.sh script found..."

BOM_VERSION="${BOM_VERSION:-Finchley.BUILD-SNAPSHOT}"
ADDITIONAL_MAVEN_OPTS="${ADDITIONAL_MAVEN_OPTS:--Dspring-cloud.version=$BOM_VERSION}"

set -e

cd $ROOT_FOLDER

echo -e "\nRunning the build with additional options [$ADDITIONAL_MAVEN_OPTS]"

# Packages all apps in parallel using 6 cores
./mvnw clean install $ADDITIONAL_MAVEN_OPTS -U --batch-mode -Dmaven.test.redirectTestOutputToFile=true
