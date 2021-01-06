import socket
import time
import threading

msg = "non"
thread = []
tdBreak = []
num = 0

def phone(sp,num):
    global msg
    global tdBreak
    lastTime = time.time()
    while tdBreak[num]:
        curTime = time.time()
        # if curTime - lastTime > 2:
        #     print("phone disconnected")
        #     break
        try:
            request = sp.recv(1024)
            # if request.decode == "alive":
            #     lastTime = time.time()
            reply = "phone say : " + request.decode() + "/"
            print(reply)
            m = reply + "\n"
            sp.send("OK\n".encode())
            # sp.sendall(reply.encode())
            msg = request.decode()
        except socket.timeout:
            pass
    
def car(sc,num):
    global msg
    global tdBreak
    lasTime = time.time()
    while tdBreak[num]:
        try:
            if msg == "go":
                sc.sendall("Car gogogo!!!".encode())
                msg = "non"
            request = sc.recv(1024)
            if request.decode() == "":
                print("Car disconnected")
                break
            reply = "Car say : " + request.decode() + "/"
            print(reply)
        except socket.timeout:
            print("car...")
            pass
    


if __name__ == "__main__":
    host = "192.168.0.197"
    port = 55688

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
                reply = "You are / " + request.decode() + " /"
                print(reply)
                con.sendall("reply\n".encode())
                if request.decode() == "phone":
                    con.settimeout(0.5)
                    pt = threading.Thread(target = phone,args=[con,num])
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
            except socket.timeout:
                pass

    except KeyboardInterrupt:
        for i in range(num):
            tdBreak[i] = False
            thread[i].join()
        s.close()
        print("Server Close")


