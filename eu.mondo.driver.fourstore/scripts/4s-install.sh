#!/bin/bash

sudo mkdir -p /var/lib/4store
sudo chown $USER:$USER /var/lib/4store/

sudo touch /etc/4store.conf
sudo chown $USER:$USER /etc/4store.conf

# for Ubuntu systems
sudo apt-get install -y software-properties-common
# for Mint 17.1+
#sudo apt-get install -y mintsources
sudo apt-add-repository "deb http://ppa.launchpad.net/yves-raimond/ppa/ubuntu precise main"
sudo apt-add-repository "deb-src http://ppa.launchpad.net/yves-raimond/ppa/ubuntu precise main"
sudo apt-get update
sudo apt-get install -y --force-yes 4store

echo "[4s-boss]
discovery = sole
nodes = ${1:-127.0.0.1}

[trainbenchmark_cluster]
port = 7890" > /etc/4store.conf
