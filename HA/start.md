安装centos 

1. sd格式化工具: https://sd-card-formatter.cn.uptodown.com/windows
2. 系统安装 https://etcher.io/
3. 扩展容量 sudo rootfs-expand 

-----------

获取小米设备token， 从小米安卓客户端缓存中读取token。

以逍遥模拟器为例：

- 启动模拟器，下载安装并打开米家APP
- 进入到逍遥模拟器的安装路径 默认:C:\Program Files\Microvirt\MEmu
-  ```adb shell``` 进入command模式执行：
```
cd /data/data/com.xiaomi.smarthome/cache/smrc4-cache
sudo grep -nr token . >> tokencahche.txt
exit
```
- ```adb pull  /data/data/com.xiaomi.smarthome/cache/smrc4-cache/tokencahche.txt```