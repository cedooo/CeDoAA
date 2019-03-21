cp homeassistant-pyvenv.service /usr/lib/systemd/system/homeassistant.service

mkdir /cedo 
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
systemctl disable firewalld.service