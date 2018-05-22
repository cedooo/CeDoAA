cp fan-control.service /usr/lib/systemd/system/fan-control.service
cp fan-control.py /cedo/fan-control.py

yum -y install python-devel
cd /cedo
wget https://files.pythonhosted.org/packages/e2/58/6e1b775606da6439fa3fd1550e7f714ac62aa75e162eed29dbec684ecb3e/RPi.GPIO-0.6.3.tar.gz \
	&& tar zvf RPi.GPIO-0.6.3.tar.gz \
	&& cd RPi.GPIO-0.6.3 \
	&& python setup.py install

systemctl enable homeassistant.service