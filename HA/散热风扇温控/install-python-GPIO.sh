yum -y install python-devel
wget https://files.pythonhosted.org/packages/e2/58/6e1b775606da6439fa3fd1550e7f714ac62aa75e162eed29dbec684ecb3e/RPi.GPIO-0.6.3.tar.gz \
	&& tar zvf RPi.GPIO-0.6.3.tar.gz \
	&& cd RPi.GPIO-0.6.3 \
	&& python setup.py install