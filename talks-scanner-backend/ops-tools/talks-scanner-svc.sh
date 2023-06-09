#!/bin/bash

APP_NAME=talks-scanner
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
WORKDIR=/opt/$APP_NAME
JAVA_OPTIONS=" -Xms256m -Xmx512m -server "
APP_OPTIONS=" --PG_HOST=$1 --PG_PORT=$2 --PG_USER=$3 --PG_PASSWORD=$4"

cd $WORKDIR
"${JAVA_HOME}/bin/java" $JAVA_OPTIONS -jar $APP_NAME.jar $APP_OPTIONS
