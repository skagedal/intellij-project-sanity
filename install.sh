#!/usr/bin/env bash

set -e

TOOL=intellij-project-sanity
BIN=${HOME}/local/bin
BUILT_BINARY=`pwd`/app/build/install/${TOOL}/bin/${TOOL}

./gradlew install
ln -fs ${BUILT_BINARY} ${BIN}/${TOOL}

