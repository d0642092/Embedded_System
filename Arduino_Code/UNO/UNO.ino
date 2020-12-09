//#include <SoftwareSerial.h>
//SoftwareSerial mySerial(5, 6); //RX=5,TX=6
int d=0;
int ledPin = 9;
void setup() {
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
}
void loop() {
//  while(!Serial.available()){}
  if(Serial.available()){
    d = Serial.parseInt();
    if(d==1){
      digitalWrite(ledPin,d);
      Serial.print("OPEN");
      Serial.flush();
    }
    else{
      digitalWrite(ledPin,d);
      Serial.print("OFF");
      Serial.flush();
    }
  }

}
