import socket

host = '127.0.0.1'
port = 8000

sc = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
sc.connect((host,port))
sc.settimeout(0.5)

try : 
    while True:
        try :
            request = input("Please input the request : ")
            sc.send(request.encode())
            reply = sc.recv(1024)
            print("I get reply : / " + reply.decode() + '/')
        except socket.timeout:
            sc.send("alive".encode())
    

except KeyboardInterrupt:
    sc.close()
    print("Client Close")