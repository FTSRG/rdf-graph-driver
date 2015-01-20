#*******************************************************************************
# Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
# Gabor Szarnyas - initial API and implementation
#*******************************************************************************
#!/bin/bash

. 4s-cluster-name.sh
SPARQL="SPARQL/vertex-pairs.sparql"

4s-query $FOURSTORE_CLUSTER_NAME "`cat $SPARQL`" -s -1 > results.txt
head -n 50 results.txt
wc -l results.txt
rm results.txt
