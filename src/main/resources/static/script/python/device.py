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

URL = "http://" + target_host +":9000/irrigation-router/devices"
bExit = False

# # your source code here 
# source_code = ''' 
# print("Hello, world!") 
# a = 1 
# b = 2 
# print(a + b) 
# '''

### variables
bSwitch = True
device_name = 'device2'
pwp = 0.0
aip = 0.0
geo = { 'latitude' : 25.03316, 'longitude' : 121.36486, 'elevation' : 23.2 }
#igs = True
ipl = 0

## for postApi
URL2 = "http://" + target_host + ":9000/irrigation-router/devices/" + device_name + "/property"
head = {'Accept': 'application/json', 'Content-Type': 'application/json'}

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

    while( not bExit ):

        if (bSwitch) :
            #API_KEY = "XXXXXXXXXXXXXXXXX"
            #PumpWaterPressure(MPa) default 0.00 - 10.00
            #ActualIrrigationPower (liters/minute) default 0-100
            pwp = round(random.uniform(1, 10),2)
            aip = random.randint(0,99)
            data = {
                    'pumpWaterPressure': pwp,
                    'actualIrrigationPower': aip,
                    'geoLocation' : geo,
                    'irrigationState' : bSwitch,
                    'irrigationPowerLevel' : ipl
                    }
            print(json.dumps(data))
            r2 = requests.patch(URL2, json.dumps(data), headers=head)
            print (r2.status_code)
 
        # wait 5 seconds
        time.sleep(5)

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
    try:
        while 1 :
            # 接收資料
            response = client.recv(1024)
            
            if(response == b'') :
                print('end connection')
                client.close()
                bExit = True
                break
            
            s_resp = response.decode("utf-8");
            cmds = s_resp.split(",")
            # 印出資料信息
            print("receive from server : " + s_resp + ".")
            
            if cmds[0] == '@switchOn' :
                print('switch on the device')
                bSwitch = True
            elif cmds[0] == '@switchOff' :
                bSwitch = False
                data = { 'irrigationState' : bSwitch }
                print(json.dumps(data))            
                r2 = requests.patch(URL2, json.dumps(data), headers=head)
                print (r2.status_code)
                print('switch off the device')
            elif cmds[0] == '@pwp' :
                print('receive set Pump Water Pressure setting : ' + cmds[1])
                pwp = float(cmds[1])
            elif cmds[0] == '@ipl' :
                print('receive set Pump Water Pressure setting : ' + cmds[1])
                ipl = float(cmds[1])
            else :
                print('Error command')         
    except:           
        bExit = True
        
    exit()
  

