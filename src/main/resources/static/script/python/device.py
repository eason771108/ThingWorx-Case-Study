#!/usr/bin/python
#coding=UTF-8
 
"""
TCP Client sample
"""
 
import socket
# importing the requests library 
import requests 
import json

target_host = "127.0.0.1"

URL = "http://" + target_host +":9000/irrigation-router/devices"
# # your source code here 
# source_code = ''' 
# print("Hello, world!") 
# a = 1 
# b = 2 
# print(a + b) 
# '''
  
# data to be sent to api 
data = {
	'deviceName' : 'device1',
	'baseTemplateName' : 'IrrigationDeviceTT'
}

headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    }
print(json.dumps(data))
# sending post request and saving response as response object 
r = requests.post(url = URL, data = json.dumps(data), headers = headers) 

print( 'Get response ' + r.text)

server_obj = json.loads(r.text)
#### to here
 
target_port = server_obj.get('servicePort')
 
# create socket
# AF_INET 代表使用標準 IPv4 位址或主機名稱
# SOCK_STREAM 代表這會是一個 TCP client
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
 
# client 建立連線
client.connect((target_host, target_port))
 
# 傳送資料給 target
msg = b'@hello'
client.sendall(msg)

while 1 :
    # 接收資料
    response = client.recv(1024)
    # 印出資料信息
    print("receive from server : " + repr(response))
