# -*- coding: utf-8 -*-  
# ���ļ�  
file = open("/sys/class/thermal/thermal_zone0/temp")  
# ��ȡ�������ת��Ϊ������  
temp = float(file.read()) / 1000  
# �ر��ļ�  
file.close()  
# �����̨��ӡ  
print "temp : %.1f" %temp  