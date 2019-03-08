#!/bin/bash

#创建工作目录
cedopath="/cedo/"
cedohasspath="${cedopath}homeassistant/"
if [ ! -d "$cedopath"] 
then 
	echo "创建工作目录"
	mkdir "$cedopath" 
else
	echo "工作目录已创建"
fi 

#准备homeassistant配置文件
if [ ! -d "$cedohasspath"] 
then 
	echo "准备homeassistant配置文件"
	mkdir "$cedohasspath"  
	cp homeassistant/configuration.yaml $cedohasspath
	cp homeassistant/customize.yaml $cedohasspath
else	
	echo "homeassistant配置文件已存在"
fi 

#安装docker-compose
if [ ! -f "/usr/local/bin/docker-compose" ] 
then 
	echo "安装docker-compose"
	sudo curl -L "https://github.com/docker/compose/releases/download/1.24.0-rc1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
	sudo chmod +x /usr/local/bin/docker-compose
else
	echo "docker-compose已安装"
fi 

#启动docker
docker-compose up --build
