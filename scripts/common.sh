#!/usr/bin/env bash

set -e

WAIT_TIME="${WAIT_TIME:-5}"
RETRIES="${RETRIES:-30}"
SERVICE_PORT="${SERVICE_PORT:-8081}"
JAVA_PATH_TO_BIN="${JAVA_HOME}/bin/"
if [[ -z "${JAVA_HOME}" ]] ; then
    JAVA_PATH_TO_BIN=""
fi
BUILD_FOLDER="${BUILD_FOLDER:-target}" #target - maven, build - gradle
HEALTH_HOST="${DEFAULT_HEALTH_HOST:-localhost}" #provide DEFAULT_HEALT HOST as host of your docker machine

# ${RETRIES} number of times will try to curl to /actuator/health endpoint to passed port $1 and localhost
function wait_for_app_to_boot_on_port() {
    curl_health_endpoint $1 "127.0.0.1" "actuator/health"
}

function default_tests() {
    appName="${1}"
    verificationString="${2}"
    echo -e "\n\nBUILDING [${appName}]\n\n"
    java_jar "${appName}"
    wait_for_app_to_boot_on_port 8080
    echo -e "\n\nA sample Withdraw command"
    curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}'
    echo -e "Verifed by a query\n\n"
    result="$( curl http://localhost:8080/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e )"
    if [[ "${result}" != *"${verificationString}"* ]]; then
        echo "Invalid response <${result}>"
        exit 1
    fi
    kill_app "${appName}"
}

function log_tailing() {
    export ADVERTISED_LISTENERS="PLAINTEXT://${EXTERNAL_IP}:9092"
    appName="with-log-tailing"
    echo -e "\n\nBUILDING [${appName}]\n\n"
    pushd with-log-tailing
        echo -e "\n\Run the infra"
        docker-compose up -d
        for i in $( seq 1 "${RETRIES}" ); do
            sleep "${WAIT_TIME}"
            echo "Tell Kafka Connect to tail transaction log of MySQL DB and send messages to Kafka"
            curl --fail -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @source.json && READY_FOR_TESTS=0 && break
            echo "Fail #$i/${RETRIES}... will try again in [${WAIT_TIME}] seconds"
        done
        if [[ "${READY_FOR_TESTS}" == "1" ]] ; then
            echo -e "\n\nKafka failed to start :(\n\n"
            print_all_logs
        fi
    popd
        echo -e "\n\Start the app"
        java_jar "${appName}"
        wait_for_app_to_boot_on_port 8080
        echo -e "A sample Withdraw command\n\n"
        curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}'
        echo "Verifed by a query (wait 2 seconds)"
        sleep 2
        result="$( curl http://localhost:8080/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e )"
        if [[ "${result}" != '[{"amount":10.00}]' ]]; then
            echo "Invalid response <${result}>"
            exit 1
        fi
    pushd with-log-tailing
        yes | docker-compose kill
    popd
    kill_app "${appName}"
}

function with_events() {
    echo -e "\n\nBUILDING [with-events]\n\n"
    pushd with-events
        echo "Run the infra"
        docker-compose up -d
        wait_for_app_to_boot_on_port 8888
        wait_for_app_to_boot_on_port 8080
        echo -e "\n\A sample Withdraw command"
        curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}'
        echo "Verifed by a query (notifce a different port: 8888!) - wait 5 seconds"
        sleep 5
        works="false"
        curl http://localhost:8888/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e | grep '"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e","amount":10.00}' && works=true
        if [[ "${works}" == 'false' ]]; then
            echo "Invalid response [${result}]"
            exit 1
        fi
        yes | docker-compose kill
        kill_app "with-events-sink"
        kill_app "with-events-source"
    popd
}

# ${RETRIES} number of times will try to curl to /health endpoint to passed port $1 and host $2
function curl_health_endpoint() {
    local PASSED_HOST="${2:-$HEALTH_HOST}"
    local HEALTH_PATH="${3:-actuator/health}"
    local READY_FOR_TESTS=1
    for i in $( seq 1 "${RETRIES}" ); do
        sleep "${WAIT_TIME}"
        curl -sSf -m 5 "${PASSED_HOST}:$1/${HEALTH_PATH}" && READY_FOR_TESTS=0 && break
        echo "Fail #$i/${RETRIES}... will try again in [${WAIT_TIME}] seconds"
    done
    if [[ "${READY_FOR_TESTS}" == "1" ]] ; then
        echo -e "\n\nThe app failed to start :( Printing all logs\n\n"
        print_all_logs
    fi
    return ${READY_FOR_TESTS}
}

# Runs the `java -jar` for given application $1 and system properties $2
function java_jar() {
    echo -e "\n\nStarting app $1 \n"
    local APP_JAVA_PATH=$1/${BUILD_FOLDER}
    local EXPRESSION="nohup ${JAVA_PATH_TO_BIN}java $2 $SYSTEM_PROPS -jar $APP_JAVA_PATH/*.jar >$APP_JAVA_PATH/nohup.log &"
    echo -e "\nTrying to run [$EXPRESSION]"
    eval ${EXPRESSION}
    pid=$!
    echo ${pid} > ${APP_JAVA_PATH}/app.pid
    echo -e "[$1] process pid is [$pid]"
    echo -e "System props are [$2]"
    echo -e "Logs are under [$APP_JAVA_PATH/nohup.log]\n"
    return 0
}

# Kills an app with given $1 version
function kill_app() {
    echo -e "Killing app $1"
    pkill -f "$1" && echo "Killed $1" || echo "$1 was not running"
    echo -e ""
    return 0
}

# Runs H2 from proper folder
function build_all_apps() {
    ${ROOT_FOLDER}/scripts/build_all.sh
}

# Kill all the apps
function kill_all_apps() {
    ${ROOT_FOLDER}/scripts/kill_all.sh
}

function print_all_logs() {
    print_logs in-one-class
}

function print_logs() {
    local app_name=$1
    echo -e "\n\nPrinting [${app_name}] logs"
    cat ${app_name}/${BUILD_FOLDER}/nohup.log || echo "Failed to print the logs"
}

function kill_all() {
    echo -e "I'm in folder `pwd`"
    echo -e "Killing all apps\n"
    kill_app in-one-class
    kill_app explicit-with-dto
    kill_app explicit-with-entity
    kill_app with-trigger
    kill_app with-log-tailing
    kill_app with-events-sink
    kill_app with-events-source
    pushd with-events
        docker-compose kill && yes | docker-compose rm -v && echo "Killed docker" || echo "Failed to kill docker"
    popd
    pushd with-log-tailing
        docker-compose kill && yes | docker-compose rm -v && echo "Killed docker" || echo "Failed to kill docker"
    popd
}

function setup_infra() {
    cd ${ROOT_FOLDER}
    echo "Starting docker-compose"
    docker-compose up -d
}

export WAIT_TIME
export RETIRES
export SERVICE_PORT

ROOT_FOLDER=`pwd`
if [[ ! -e "${ROOT_FOLDER}/.git" ]]; then
    cd ..
    ROOT_FOLDER=`pwd`
    if [[ ! -e "${ROOT_FOLDER}/.git" ]]; then
        echo "No .git folder found"
        exit 1
    fi
fi

mkdir -p "${ROOT_FOLDER}/${BUILD_FOLDER}"
