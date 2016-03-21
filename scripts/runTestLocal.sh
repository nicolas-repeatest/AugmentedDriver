#!/bin/bash

#
# Runs a test
#
# @param $1 the class (for example net.avantica.tests.smoke.LoadList) to run.
# @param $2 the test to run
# @param $3: the capabilities file to user, by default capabilities/chrome.capabilities
# @param $4: the number of times the test is going to run. By default is 1.
# @param $5: how many mtests are going to run in parallel. By default is 1.

CLASS=$1
TEST=$2
if [[ -z "$3" ]]; then
    CAPABILITIES="capabilities/chrome.capabilities"
else
    CAPABILITIES=$3
fi
if [[ -z "$4" ]]; then
    QUANTITY="1"
else
    QUANTITY=$4
fi
if [[ -z "$5" ]]; then
    PARALLEL_TESTS="1"
else
    PARALLEL_TESTS=$5
fi

echo "Running test for ${CLASS}#${TEST} quantity ${QUANTITY} parallel tests ${PARALLEL_TESTS} with capabilities ${CAPABILITIES}"

rm -rf target/
./gradlew runTest -Pclazz=${CLASS} -Ptest=${TEST} -Pquantity=${QUANTITY} -PparallelTests=${PARALLEL_TESTS} -Pcapabilities=${CAPABILITIES}
