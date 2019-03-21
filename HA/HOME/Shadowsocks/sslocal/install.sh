cp ss.service /usr/lib/systemd/system/ss.service

curl "https://bootstrap.pypa.io/get-pip.py" -o "get-pip.py" && python get-pip.py
pip install shadowsocks
# nohup ssserver -c /cedo/shadowsocks.json &
# sslocal -c /cedo/shadowsocks.json -d start
systemctl enable ss.service