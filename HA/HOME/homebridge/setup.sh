cd /cedo
wget https://nodejs.org/dist/v8.11.2/node-v8.11.2-linux-armv7l.tar.xz
xz -d node-v8.11.2-linux-armv7l.tar.xz
tar -xvf node-v8.11.2-linux-armv7l.tar
mv node-v8.11.2-linux-armv7l nodejs
ln -s /cedo/nodejs/bin/node /usr/bin/node
ln -s /cedo/nodejs/bin/npm /usr/bin/npm
mkdir ~/.homebridge/

sudo npm install -g --unsafe-perm homebridge
sudo npm install -g homebridge-homeassistant
