FROM centos:7

ENV SSGITURL https://github.com/cedooo/shadowsocks.git@master

RUN buildDeps='git wget' \
	&& yum install -y $buildDeps \
	# 切换到阿里国内源
	&& mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup \
	&& wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo \
	&& yum clean all \
	&& yum makecache \
	&& yum update -y \
	# 开始下载安装
	&& yum install -y python-setuptools \
	&& easy_install pip \ 
	&& pip install git+$SSGITURL \
	# 移除遗留组件
    && yum remove -y $buildDeps \
	&& mkdir /cedo
	
WORKDIR /cedo
	
COPY shadowsocks.json /cedo/

EXPOSE 32811/tcp
EXPOSE 32811/udp

CMD sslocal -c ./shadowsocks.json start