import socket
import time
import threading
# from Embedded_System.DataBase import database

msg = "non"
pf = True
cf = True

def phone(sp):
    global msg
    while pf:
        try:
            request = sp.recv(1024)
            reply = "phone say : " + request.decode() + "/\n"
            sp.sendall(reply.encode())
            msg = request.decode()
        except socket.timeout:
            pass
    
def car(sc):
    global msg
    while cf:
        try:
            request = sc.recv(1024)
            reply = "Car say : " + request.decode() + "/\n"
            print(reply)
            sc.sendall(reply.encode())
        except socket.timeout:
            if msg == "get":
                sc.sendall("Car gogogo!!!".encode())
                msg = "non"
    


if __name__ == "__main__":

    host = "192.168.0.199"
    port = 55688

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host,port)) # 固定ip與port
    s.listen(1) # 監聽最大數量
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
                con.sendall(reply.encode())
                if request.decode() == "phone":
                    con.settimeout(0.5)
                    pt = threading.Thread(target = phone,args=[con])
                    pt.start()
                elif request.decode() == "car_WiFi":
                    con.settimeout(0.5)
                    ct = threading.Thread(target=car,args=[con])
                    ct.start()
                else:
                    con.close()
            except socket.timeout:
                print(".")

    except KeyboardInterrupt:
        cf = False
        pf = False
        try :
            pt.join()
            ct.join()
        except NameError:
            pass
        s.close()
        print("Server Close")


