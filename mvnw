#!/bin/sh
# Licensed to the Apache Software Foundation (ASF)
# Maven Wrapper for Unix/Linux/Mac
# This shell script will download Maven and execute Maven if necessary.

BASE_DIR=$(dirname "$0")
if [ -z "$BASE_DIR" ]; then
  BASE_DIR="."
fi
BASE_DIR=$(cd "$BASE_DIR" && pwd)

MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-"$BASE_DIR"}
MAVEN_OPTS="${MAVEN_OPTS}"
MAVEN_CMD_LINE_ARGS="$@"

export MAVEN_PROJECTBASEDIR
export MAVEN_OPTS
export MAVEN_CMD_LINE_ARGS

MAVEN_HOME="${MAVEN_HOME}"
if [ -z "$MAVEN_HOME" ]; then
  MAVEN_HOME="$MAVEN_PROJECTBASEDIR/.mvn"
fi

if [ -d "$MAVEN_HOME" ]; then
  MAVEN_EXECUTABLE="$MAVEN_HOME/bin/mvn"
else
  echo "Error: MAVEN_HOME is not set and maven directory not found."
  exit 1
fi

if [ ! -f "$MAVEN_EXECUTABLE" ]; then
  echo "Error: $MAVEN_EXECUTABLE does not exist."
  exit 1
fi

exec "$MAVEN_EXECUTABLE" $MAVEN_CMD_LINE_ARGS
