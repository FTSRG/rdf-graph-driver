#!/bin/bash

cd "$( cd "$( dirname "$0" )" && pwd )/.."

while [ "$1" != "" ]; do
	case $1 in
		"--skipTests")
		skipTests=true
		;;
		"--resolveDependencies")
		resolveDependencies=true
		;;
	esac
	shift
done

export MAVEN_OPTS="-Xmx512m"

if [ $resolveDependencies ]; then
	scripts/resolve-dependencies.sh || exit 1
fi

if [ $skipTests ]; then
	mvn clean install -DskipTests || exit 1
else
	mvn clean install || exit 1
fi
