4store-graph-driver
===================

This repository contains the Maven project for the Java driver of 4store, used in the Train Benchmark and IncQuery-D. The primary goals of the projects are the following:
* support a graph-like operation,
* provide high transaction performance by using the command-line interface of 4store (4s-query, 4s-update, etc.) instead of the 4store HTTP server (4s-httpd).

Prerequisites
-------------

The `scripts` directory contains the scripts for installing 4store.

### Installing 4store

* On Debian-based systems, you may install 4store as a package using the `4s-install.sh` script. As of August 2014, the APT repository contains an outdated version of 4store, so the script uses a PPA repository instead.
* On Debian-based systems, you may use the `4s-install-from-source.sh` install the 4store 1.1.5 from source.
* On other systems, consult the installation guide of 4store: <http://4store.org/trac/wiki/Install>.

The client is tested with 4store 1.1.5.

### Dependencies

The 4store-graph-driver requires `rdf-graph-driver` to work. You may use the `resolve-dependencies.sh` script to download it.

Build
-----

Use the `scripts/build-with-dependencies.sh` script to build this project with its dependencies.

If you want to build the `4store-graph-driver` project only, use the `scripts/build.sh` script.

Running 4store
--------------

* Run the `4s-init.sh` script to initialize the `/etc/4store.conf` file. If you have a cluster, pass the host name list as argument, e.g. `vm0;vm1`.

* Use the `4s-start.sh` script to start 4store.
