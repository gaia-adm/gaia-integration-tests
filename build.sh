#!/bin/bash

function setup_container()
{
    echo "Setting up container.."

    cd /src
    mvn clean install
}

setup_proxy

if [ $? -eq 0 ];then
    setup_container
else
    echo "Unable to continue setup"
    exit 1
fi