#!/bin/bash
# Build the Docker images which contains the plugins
# and a pre-defined config.xml
docker build --no-cache --rm=true -t jenkins-with-plugins .
