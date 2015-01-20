#!/bin/bash

cd "$( cd "$( dirname "$0" )" && pwd )"

cd ../..
git clone https://github.com/FTSRG/rdf-graph-driver.git
cd rdf-graph-driver
git pull
scripts/build.sh --resolveDependencies
