#!/bin/bash

#
# Runs a Suite locally
#
# Selenium (see ./scriots/startSelenium.sh)
#
# @param $1: The suites to run
# @param $2: The suites package
# @param $3: capabilities, by default chrome
# @param $4: Number of tests to run in parallel. Default is 1
#

SUITES=$1
SUITES_PACKAGE=$2
if [[ -z "$3" ]]; then
    CAPABILITIES="chrome"
else
    CAPABILITIES=$3
fi
if [[ -z "$4" ]]; then
    PARALLEL_TESTS="1"
else
    PARALLEL_TESTS=$4
fi

echo "Running suites for ${SUITES} on Suites Package ${SUITES_PACKAGE}. Running ${PARALLEL_TESTS} on parallel on capabilities ${CAPABILITIES}"

rm -rf ./target

./gradlew runSuite -Psuites=${SUITES} -PsuitesPackage=${SUITES_PACKAGE} -PparallelTests=${PARALLEL_TESTS} -Pcapabilities=${CAPABILITIES}
