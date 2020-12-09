import _thread
import socket
import threading

# HOSTip = '192.168.8.104'
HOSTip = '192.168.43.214'
PORTnum = 55688
ClientMaxNum = 2

def client_handler_arduino(connection):
    connection.send(str.encode('I\'m Ranyu\'s Raspberrypi\n'))

    while True:
        clientMessage = str(connection.recv(1024), encoding='utf-8')
        print('Client:{}'.format(clientMessage))
def client_handler_phone(connection:socket):
    # connection.send('I\'m Ranyu\'s Raspberrypi\n'.encode('utf-8'))
    connection.send('Ran'.encode("utf-8"))
    clientMessage = str(connection.recv(1024), encoding='utf-8')
    if clientMessage == "OK":
        while True:
            a = input("Input number: ")
            if a=="0" or a=="1":
                connection.send(a.encode("utf-8"))
                clientMessage = str(connection.recv(1024), encoding='utf-8')
                print('Client:{}'.format(clientMessage))


class rasp_server(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.clientThreadCount = 0
    def run(self):
        HOST = HOSTip
        PORT = PORTnum

        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind((HOST, PORT))
        server.listen(ClientMaxNum)

        print('Waiting for Connection...')
        while True:
            # 等待任一Client連線
            Client, address = server.accept()
            print('Connected to: {}:{}'.format(address[0], str(address[1])))
            # 開啟兩端溝通Thread
            clientIdentify = str(Client.recv(1024), encoding="utf-8")

            if clientIdentify == "I'm NodeMCU":
                print(clientIdentify)
                threading.Thread(target=client_handler_phone, args=(Client,)).start()
            self.clientThreadCount += 1
            print('Thread Number: {}'.format(self.clientThreadCount))

if __name__ == '__main__':
    # HOSTip = input('IP =')
    # PORTnum = input('PORT = ')
    ServerTd = rasp_server()
    ServerTd.start()
