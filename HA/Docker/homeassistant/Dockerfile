FROM centos:7
MAINTAINER chendong<cedoogle@gmail.com>

ENV PYTHON_VERSION 3.7.2

RUN buildDeps='zlib-devel zlib-static sqlite-devel gcc automake autoconf libtool make openssl-devel' \
	&& mkdir -p /cedo \
	&& mkdir /usr/local/python3 \
	&& yum -y install wget \ 
	# 切换到阿里国内源
	&& mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup \
	&& wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo \
	&& yum clean all \
	&& yum makecache \
	&& yum update -y \
	&& yum -y install $buildDeps \ 
	&& wget https://www.python.org/ftp/python/$PYTHON_VERSION/Python-$PYTHON_VERSION.tgz /cedo/


WORKDIR /cedo/
RUN tar xvf Python-$PYTHON_VERSION.tgz

WORKDIR /cedo/Python-$PYTHON_VERSION
RUN ./configure -prefix /usr/local/python3 \
    && make \
    && make install \
	&& ln -s /usr/local/python3/bin/python3 /usr/bin/python3 \
    && ln -s /usr/local/python3/bin/pip3 /usr/bin/pip3 \
    && python3 -m pip install -U pip \
    && python3 -m pip install homeassistant \
	&& ln /usr/local/python3/bin/hass /usr/bin/hass

EXPOSE 8123

CMD "hass --open-ui"