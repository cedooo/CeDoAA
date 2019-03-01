FROM centos:7

ENV SSGITURL https://github.com/cedooo/shadowsocks.git@master

RUN buildDeps='git' \
	&& yum update -y \
	&& yum install -y $buildDeps \
	&& yum install -y python-setuptools \
	&& easy_install pip \ 
	&& pip install git+$SSGITURL \
    && yum remove -y $buildDeps \
	&& mkdir /cedo
	
WORKDIR /cedo
	
COPY shadowsocks.json /cedo/

EXPOSE 32811/tcp
EXPOSE 32811/udp

CMD sslocal -c ./shadowsocks.json start