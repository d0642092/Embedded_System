import socket
import time
import threading
from Embedded_System.DataBase import database
import sqlite3

msg = "non"
thread = []
tdBreak = []
num = 0

def phone(sp,num,cursor):
    global msg
    global tdBreak
    while tdBreak[num]:
        try:
            request = sp.recv(1024).decode()
            lineSplit = request.split('/')
            if request == "":
                print("phone disconnected")
                break
            elif request == "alive":
                pass
            elif lineSplit[0] == "get" or lineSplit[0] == "store":
                reply = "OK\n"
                sp.sendall(reply.encode())
                while msg == "go":
                    pass
                msg = "go"
                print(request)
            elif lineSplit[0] == "get":
                reply = "OK\n"
                sp.sendall(reply.encode())
                database.update(cur,)
        
        except socket.timeout:
            pass
    
def car(sc,num):
    global msg
    global tdBreak
    lastTime = time.time()
    while tdBreak[num]:
        curTime = time.time()
        if curTime - lastTime > 2:
            print("Car disconnected")
            break
        try:
            if msg == "go":
                sc.sendall("CarGo".encode())
                msg = "non"
            request = sc.recv(1024)
            reply = "Car say : " + request.decode() + "/\n"
            print(reply)
            lastTime = time.time()
        except socket.timeout:
            pass
    


if __name__ == "__main__":
    host = "192.168.50.1"
    port = 55688

    with sqlite3.connect("rasp.db") as db:
        print("open database...")
        cur = db.cursor()
        database.createTable(c)

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host,port)) # 固定ip與port
    s.listen(3) # 監聽最大數量
    s.settimeout(5)

    try : 
        while True:
            try : 
                con,addr =  s.accept()
                print(con,"//",addr)
                request = con.recv(1024)
                print("request : ",request.decode())
                reply = "You are / " + request.decode() + " /\n"
                print(reply)
                con.sendall(reply.encode())
                if request.decode() == "phone":
                    con.settimeout(0.5)
                    pt = threading.Thread(target = phone,args=[con,num,cur])
                    tdBreak.append(True) # 保留打斷每個thread的bealoon
                    thread.append(pt) # 儲存每個thread
                    num = num + 1 # 紀錄有幾個thread
                    pt.start()
                elif request.decode() == "car_WiFi":
                    con.settimeout(0.5)
                    ct = threading.Thread(target=car,args=[con,num])
                    tdBreak.append(True) # 保留打斷每個thread的bealoon
                    thread.append(ct) # 儲存每個thread
                    num = num + 1 # 紀錄有幾個thread
                    ct.start()
                else:
                    con.close()
                print("thread : ", num)
            except socket.timeout:
                pass

    except KeyboardInterrupt:
        for i in range(num):
            tdBreak[i] = False
            thread[i].join()
        s.close()
        print("Server Close")


