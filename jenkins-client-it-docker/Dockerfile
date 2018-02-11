FROM jenkins/jenkins:2.89.3-alpine
MAINTAINER "docker@soebes.de"
COPY ./config.xml /usr/share/jenkins/config.xml
COPY ./plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
