#!/usr/bin/env bash

source common.sh || source scripts/common.sh || echo "No common.sh script found..."

set -e

export BOM_VERSION="Finchley.BUILD-SNAPSHOT"
export EXTERNAL_IP
EXTERNAL_IP="$( "${ROOT_FOLDER}"/scripts/whats_my_ip.sh )"

echo "External IP equals [${EXTERNAL_IP}]"

echo "Ensure that apps are not running"
kill_all_apps

echo -e "Ensure that all the apps are built with $BOM_VERSION!\n"
build_all_apps

cat <<EOF

This Bash file will run all the apps required for [${BOM_VERSION}] tests.

NOTE:

- you need internet connection for the apps to download configuration from Github.
- you need docker-compose

We will do it in the following way:
01) Commands and queries handled in one class (no CQRS)
02) CQRS with application service as explicit synchronization
03) CQRS with application service as explicit synchronization (JPA)
04) CQRS with trigger as implicit synchronization
05) CQRS with transaction log tailing as synchronization
06) CQRS with Domain Events as synchronization

EOF

function run_tests() {
    default_tests "in-one-class" '[{"amount":10.00}]'
    default_tests "explicit-with-dto" '[{"cardId":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e","amount":10.00}]'
    default_tests "explicit-with-entity" '[{"amount":10.00,"cardId":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e"}]'
    default_tests "with-application-events" '[{"cardId":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e","amount":10.00}]'
    default_tests "with-trigger" '[{"cardId":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e","amount":10.00}]'
    log_tailing
    with_events
}

cd ${ROOT_FOLDER}

run_tests
kill_all
