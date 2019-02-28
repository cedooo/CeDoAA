FROM centos:7

RUN buildDeps='git' \
	&& yum update -y \
	&& yum install -y $buildDeps \
	&& yum install -y python-setuptools \
	&& easy_install pip \ 
	&& pip install git+https://github.com/cedooo/shadowsocks.git@master \
    && yum remove -y $buildDeps
COPY shadowsocks.json /etc/

# EXPOSE 32811/tcp
# EXPOSE 32811/udp

CMD ssserver -c /etc/shadowsocks.json start