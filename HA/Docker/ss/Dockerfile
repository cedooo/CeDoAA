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

# 使用shadowsocks.json配置使用
# CMD sslocal -c ./shadowsocks.json start

# 通过docker run 指定配置
# docker run cd:t -s 服务器地址 -p 服务器端口 -l 本地端端口 -k 密码 -m 加密方法
# 例如:  docker run cd:t -s 1.2.3.4 -p 65533 -l 32811 -k 服务器ss密码 -m aes-256-cfb
ENTRYPOINT ["sslocal"] 