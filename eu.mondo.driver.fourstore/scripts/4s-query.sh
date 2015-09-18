#!/bin/bash

cd "$( cd "$( dirname "$0" )" && pwd )"
. 4s-cluster-name.sh

4s-query $FOURSTORE_CLUSTER_NAME -f text "$1" -s -1

