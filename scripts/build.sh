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

export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=256m"

if [ $resolveDependencies ]; then
	scripts/resolve-dependencies.sh || exit 1
fi

mvn clean install -P file || exit 1
