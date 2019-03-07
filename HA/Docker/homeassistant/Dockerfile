FROM centos/python-36-centos7
MAINTAINER chendong<cedoogle@gmail.com>

WORKDIR /cedo/ 
RUN pip install homeassistant \
	&& ln /usr/local/python3/bin/hass /usr/bin/hass

EXPOSE 8123/tcp
EXPOSE 8123/udp

ENTRYPOINT ["hass"]