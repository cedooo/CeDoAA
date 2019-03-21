FROM centos/python-36-centos7
MAINTAINER chendong<cedoogle@gmail.com>

RUN pip install --upgrade pip \
	&& pip install homeassistant 

EXPOSE 8123/tcp
EXPOSE 8123/udp

ENTRYPOINT ["hass"]