#! /bin/sh

export JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9999"

#exec sbt -Dconfig.file=conf/settings.conf "$@"
exec sbt "$@"
