#!/bin/bash
# homeassistant
cp service/homeassistant-pyvenv.service /usr/lib/systemd/system/homeassistant.service
cp config/configuration.yaml /cedo/configuration.yaml
# shadowsocks
cp service/ss.service /usr/lib/systemd/system/ss.service
cp config/shadowsocks.json /cedo/shadowsocks.json
# 风扇
cp service/fan-control.service /usr/lib/systemd/system/fan-control.service
cp driver/fan-control.py /cedo/fan-control.py


systemctl disable firewalld.service
# homeassistant 助手
cd /cedo
yum -y update && yum -y install wget && wget --no-check-certificate "https://www.python.org/ftp/python/3.6.5/Python-3.6.5.tgz" && tar xvf Python-3.6.5.tgz
yum -y install zlib-devel zlib-static sqlite-devel gcc automake autoconf libtool make  openssl-devel sudo 
# for 小米网关和百度语言
yum -y install libffi-devel libjpeg-turbo-devel
cd Python-3.6.5
mkdir /usr/local/python3
./configure -prefix /usr/local/python3
make
make install
ln -s /usr/local/python3/bin/python3 /usr/bin/python3
ln -s /usr/local/python3/bin/pip3 /usr/bin/pip3
python3 -m pip install -U pip

cd /cedo
python3 -m venv homeassistant
cd homeassistant
source bin/activate
python3 -m pip install -U pip
python3 -m pip install wheel
python3 -m pip install homeassistant

systemctl enable homeassistant.service

# shadowsocks 服务
curl "https://bootstrap.pypa.io/get-pip.py" -o "get-pip.py" && python get-pip.py
pip install shadowsocks
# nohup ssserver -c /cedo/shadowsocks.json &
# sslocal -c /cedo/shadowsocks.json -d start
systemctl enable ss.service

#风扇
yum -y install python-devel
cd /cedo
wget https://files.pythonhosted.org/packages/e2/58/6e1b775606da6439fa3fd1550e7f714ac62aa75e162eed29dbec684ecb3e/RPi.GPIO-0.6.3.tar.gz \
	&& tar zvf RPi.GPIO-0.6.3.tar.gz \
	&& cd RPi.GPIO-0.6.3 \
	&& python setup.py install

systemctl enable fan-control.service