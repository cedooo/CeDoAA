FROM centos:7
MAINTAINER chendong<cedoogle@gmail.com>

ENV PYTHON_VERSION 3.6.5

RUN mkdir -p /cedo
RUN mkdir /usr/local/python3

# COPY Python-3.6.5.tgz /cedo/
ADD https://www.python.org/ftp/python/3.6.5/Python-3.6.5.tgz /cedo/

RUN yum -y install zlib-devel zlib-static sqlite-devel gcc automake autoconf libtool make openssl-devel wget sudo

WORKDIR /cedo/
RUN tar xvf Python-3.6.5.tgz

WORKDIR /cedo/Python-3.6.5
RUN ./configure -prefix /usr/local/python3 && \
    make && \
    make install
RUN ln -s /usr/local/python3/bin/python3 /usr/bin/python3 && \
    ln -s /usr/local/python3/bin/pip3 /usr/bin/pip3 && \
    python3 -m pip install -U pip  && \
    python3 -m pip install homeassistant
RUN ln /usr/local/python3/bin/hass /usr/bin/hass

EXPOSE 8123

CMD "nohup hass --open-ui &"