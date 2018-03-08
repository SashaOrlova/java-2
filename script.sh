#!/bin/bash

rootdir=$(pwd)
for mvnfile in $(find . -name pom.xml); do
    dir=$(dirname $mvnfile)
    echo 'Building' $dir
    cd $dir
    mvn test
    if [ $? -ne 0 ]; then
        echo "Error while running in "
        echo $dir
        exit 1
    fi
    cd $rootdir
done