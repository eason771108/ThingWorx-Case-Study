#!/usr/bin/python
#coding=UTF-8
 
"""
TCP Client sample
"""
 
import socket
# importing the requests library 
import requests 
import json

import requests 
import json
import time
import threading # for threading
import os
import random

### setting
target_host = "127.0.0.1"
device_name = 'device1'

URL = "http://" + target_host +":9000/irrigation-router/devices"
# # your source code here 
# source_code = ''' 
# print("Hello, world!") 
# a = 1 
# b = 2 
# print(a + b) 
# '''

### variables
bSwitch = True;

# data to be sent to api 
data = {
	'deviceName' : device_name,
	'baseTemplateName' : 'IrrigationDeviceTT'
}

headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    }
    
def PostApi():

    while(1):

        if (bSwitch) :
            URL2 = "http://" + target_host + ":9000/irrigation-router/devices/" + device_name + "/property"
            #API_KEY = "XXXXXXXXXXXXXXXXX"
            #PumpWaterPressure(MPa) default 0.00 - 10.00
            #ActualIrrigationPower (liters/minute) default 0-100
            data = {'pumpWaterPressure': round(random.uniform(1, 10),2),
                    'actualIrrigationPower': random.randint(0,99)}
            head = {'Accept': 'application/json',
                    'Content-Type': 'application/json'}
            print(json.dumps(data))
            r2 = requests.patch(URL2, json.dumps(data), headers=head)
            print(json.dumps(data))
            
            print (r2.status_code)
        # wait 5 seconds
        time.sleep(5)
        #a = a -1

if __name__ == '__main__':
    #start_time = time.time()
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
    
    device_thread = threading.Thread(target=PostApi)  # 例項化一個執行緒物件，使執行緒執行這個函式
    device_thread .start()  # 啟動這個執行緒

    #device_thread .join()  # 等待thread_1結束
    #os._exit(0)
    #end_time = time.time()

    while 1 :
        # 接收資料
        response = client.recv(1024)
        # 印出資料信息
        print("receive from server : " + response.decode("utf-8") + ".")
        if response == b'@switchOn' :
            print('switch on the device')
            bRun = True
        elif response == b'@switchOff' :
            bRun = False
            print('switch off the device')
        else :
            print('Error command')         

  

