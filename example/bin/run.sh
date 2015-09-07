#!/bin/bash
# Copyright (c) 2015 Shumei Inc. All Rights Reserved.
# Authors: Liang Kun <liangkun@ishumei.com>

if [ $# -ne 2 ]; then
  echo "USAGE: $0 broker-list topic"
  exit 1
fi

base_dir=$(dirname $0)/..

# Which java to use
if [ -z "$JAVA_HOME" ]; then
  JAVA="java"
else
  JAVA="$JAVA_HOME/bin/java"
fi

# Memory options
if [ -z "$KAFKA_HEAP_OPTS" ]; then
  KAFKA_HEAP_OPTS="-Xmx256M"
fi

# JVM performance options
if [ -z "$KAFKA_JVM_PERFORMANCE_OPTS" ]; then
  KAFKA_JVM_PERFORMANCE_OPTS="-server -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:+CMSScavengeBeforeRemark -XX:+DisableExplicitGC -Djava.awt.headless=true"
fi

exec $JAVA -jar "$base_dir/target/kafka-console-producer-1.0-jar-with-dependencies.jar" "$@"
