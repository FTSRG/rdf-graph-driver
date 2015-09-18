#!/bin/bash

cd "$( cd "$( dirname "$0" )" && pwd )"

nodes=`grep nodes /etc/4store.conf | sed "s/ //"`
node_list=`echo $nodes | awk -F'=' '{print $2}'`
node_array=$(echo $node_list | tr ";" "\n")

for node in $node_array; do
	if [ $node = "localhost" ] || [ $node = "127.0.0.1" ]; then
		echo "Starting 4store on localhost"
		4s-boss
	else
		echo "Starting 4store on remote node: $node"
		ssh $node "4s-boss"
	fi
done

echo "Grep 4s-boss"
ps auxw | grep 4s-bos[s]

[ -n "$FOURSTORE_CLUSTER_NAME" ] || . 4s-cluster-name.sh

echo "Creating store: $FOURSTORE_CLUSTER_NAME"
4s-admin create-store $FOURSTORE_CLUSTER_NAME
sleep 2

echo "Starting store: $FOURSTORE_CLUSTER_NAME"
4s-admin start-stores $FOURSTORE_CLUSTER_NAME
sleep 2

echo "Listing nodes"
4s-admin list-nodes

sleep 2
echo "Listing stores"
4s-admin list-stores

sleep 1
