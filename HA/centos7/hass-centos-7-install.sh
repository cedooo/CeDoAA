cp homeassistant.service /usr/lib/systemd/system/homeassistant.service
yum -y install zlib-devel zlib-static  sqlite-devel  gcc automake autoconf libtool make  openssl-devel wget sudo
mkdir /cedo
cd /cedo/
wget --no-check-certificate https://www.python.org/ftp/python/3.6.5/Python-3.6.5.tgz
tar xvf Python-3.6.5.tgz
cd Python-3.6.5
mkdir /usr/local/python3
./configure -prefix /usr/local/python3
make
make install
ln -s /usr/local/python3/bin/python3 /usr/bin/python3
ln -s /usr/local/python3/bin/pip3 /usr/bin/pip3
python3 -m pip install -U pip
python3 -m pip install homeassistant
ln /usr/local/python3/bin/hass /usr/local/hass

systemctl enable homeassistant.service
systemctl disable firewalld.service