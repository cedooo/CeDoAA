FROM centos:7
MAINTAINER chendong<cedoogle@gmail.com>

RUN mkdir /cedo
WORKDIR /cedo
COPY hass-centos-7-pyvenv-install.sh /cedo/install.sh
COPY homeassistant-pyvenv.service /cedo/

EXPOSE 8123