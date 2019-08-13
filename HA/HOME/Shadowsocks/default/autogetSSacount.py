import json
import os
import psutil
import time

import cfscrape
from lxml import html
#get ss from web

def getFreeSS():
    youneedSSUrl = "https://www.youneed.win/free-ss"
    sp = cfscrape.create_scraper()  # returns a CloudflareScraper instance
    # Or: scraper = cfscrape.CloudflareScraper()  # CloudflareScraper inherits from requests.Session
    proxy = {"http": "http://localhost:10811", "https": "http://localhost:10811"}
    sp.proxies = proxy
    scraper = cfscrape.create_scraper(sess=sp)
    rsponse = scraper.get(youneedSSUrl)
    # print(rsponse.text)  # => "<!DOCTYPE html><html><head>..."
    if (rsponse.status_code == 200):
        responseHtml = rsponse.text
        tree = html.fromstring(responseHtml)
        ssservers = tree.xpath("//section[@class='context']/table/tbody/tr")
        ssserversCount = len(ssservers)
        servers = [];
        for s in range(ssserversCount):
            serverObjAttrs = tree.xpath("//section[@class='context']/table/tbody/tr/td/text()")
            startIndex = 6*s
            ip = serverObjAttrs[startIndex+0]
            port = serverObjAttrs[startIndex+1]
            passwd = serverObjAttrs[startIndex+2]
            method = 'aes-256-cfb'# serverObjAttrs[startIndex+3] #aes-256-cfb or
            tm = serverObjAttrs[startIndex+4]
            country = serverObjAttrs[startIndex+5]
            sjson = {"server": ip, "server_port": int(port), "password": passwd, "method": method, "remarks": country+"[" + time.strftime("%Y-%m-%d ", time.localtime())+tm+"]"}
            servers.append(sjson)
        return servers
    else:
        return []


def getWinPid(pname):
    pids = psutil.pids()
    for pid in pids:
        p = psutil.Process(pid)
        if pname == p.name():
            return pid
    return -1
programPath = os.path.dirname(os.path.abspath(__file__))
fileSep = os.path.sep
programeName = "Shadowsocks.exe"
file_Shadowsocks_config = programPath + fileSep + "gui-config.json"
pFullPath = programPath + fileSep + programeName

def loadWinAndRestartWinShadowsocks():

    pid = getWinPid(programeName)
    if(pid>0):
        os.popen('TASKKILL /F /PID ' + str(pid))
        print("关闭%s，pid=%d" % (programeName, pid))
        time.sleep(5)
    os.system("START /B " + pFullPath)
    print("启动%s" % programeName)


if __name__ == "__main__":
    shaPid = getWinPid(programeName)
    if shaPid < 0:
        os.system("START /B " + pFullPath)
    try: 
        ssserver = getFreeSS()
    except:
        print("获取账号失败！")
        ssserver = [] 
    if len(ssserver) > 0: 
        print(ssserver)
        with open(file_Shadowsocks_config, 'r') as f:
            data = json.load(f)
            data['configs'] = ssserver
        with open(file_Shadowsocks_config, 'w') as f:
            json.dump(data, f)
        loadWinAndRestartWinShadowsocks()
    else:
        print("更新账号失败!")
