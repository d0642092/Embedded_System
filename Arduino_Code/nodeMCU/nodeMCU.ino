#include <ESP8266WiFi.h>
//#include <SoftwareSerial.h>
//SoftwareSerial mySerial(D3, D1); //RX=D3,TX=D1
const char* ssid = "HUAWEI nova 4e";
const char* password = "c123f339852d";
//const char* ssid = "HUAWEI-B525-436B";
//const char* password = "MDQYY90R75R";
char str[] = "I'm NodeMCU";
int timeout = 0;
//String serverIP = "192.168.8.104";
String serverIP = "192.168.43.214";
int port = 55688;
WiFiClient client;

boolean flag = true;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  WiFi.hostname("NodeMCU");
  WiFi.begin(ssid, password);
  while( WiFi.status()!= WL_CONNECTED){
    delay(500);
  }
  
  
}

void loop() {
  if(client.connected()){
    if(flag){
      client.stop();
      flag = false;
    }
    if(client.available()){
      timeout = client.parseInt();
      Serial.print(timeout);
      Serial.flush();
    }
    if(Serial.available()){
        String respond = Serial.readString();
        client.print(respond);
        client.flush();
    }
  }
  else{
    if(client.connect(serverIP, port)){
      client.print(str);
      client.flush();
      if(client.available()){
        String content = client.readString();
        if(content != "Ran"){
          client.stop();
        }
        else{
          client.print("OK");
          client.flush();
        }
      }
    }
  }
  
}
