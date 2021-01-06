import socket
import time
import RPi.GPIO as GPIO
import threading

carIP = ""
userIP = ""

def setup():
    motorPin = [1,2,3,4,5,6,7,8]
    GPIO.setmode(GPIO.BOARD)
    for i in range(len(motorPin)):
        GPIO.setup(motorPin[i],GPIO.OUT)

def serverSet():  
    host = ''
    port = 8000
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.bind((host,port))
    s.listen(2)
    return s

def waitConnect(s):
    con,addr = s.accept()
    print("connect success, con,IP : ",con,addr)
    identify = con.recv(16)
    if identify == "user":
        userIP = addr[1]
    else :
        carIP = addr[1]
    return con

def Control(conUser,conCar):
    command_go = "come here"
    command_back = "back home"
    while True:
        conCar.send(command_go)
        conCar.send(command_back)


if __name__ == "__main__":
    setup()
    s = serverSet()
    conCar = waitConnect(s)
    


