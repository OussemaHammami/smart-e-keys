#include<TinyGPS++.h>
#include<HardwareSerial.h>
#include<BluetoothSerial.h>
#include <ESP32Servo.h>

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run 'make menuconfig' to and enable it
#endif

BluetoothSerial BT;
static const int RXPin=16,TXPin=17;
TinyGPSPlus gps;

char recieved;
const char Location ='A';
const char Open ='O';

int pos = 0;    // variable to store the servo position
// Recommended PWM GPIO pins on the ESP32 include 2,4,12-19,21-23,25-27,32-33 
int servoPin = 18;
Servo myservo;

void setup() {
  // put your setup code here, to run once:
 Serial.begin(9600);
 Serial1.begin(9600, SERIAL_8N1, RXPin, TXPin);
 BT.begin("Clio");
 //SERVO
 // Allow allocation of all timers
 ESP32PWM::allocateTimer(0);
  ESP32PWM::allocateTimer(1);
  ESP32PWM::allocateTimer(2);
  ESP32PWM::allocateTimer(3);
  myservo.setPeriodHertz(50);    // standard 50 hz servo
  myservo.attach(servoPin, 500, 2400); // attaches the servo on pin 18 to the servo object
  // using default min/max of 1000us and 2000us
  // different servos may require different min/max settings
  // for an accurate 0 to 180 sweep

}

void loop() {
 // / put your main code here, to run repeatedly:
while (Serial1.available())if (gps.encode(Serial1.read()));
Serial.println(Serial1.read());
if (BT.available()) 
 {
  recieved=(char)(BT.read());
  Serial.println(recieved);
  if (recieved=='A') {display_location();}
  if (recieved=='O') {moteuropen();}
  if (recieved=='C') {moteurclose();}
 }

if (millis()>5000 && gps.charsProcessed()<10)
{ BT.print(F("No gps detected"));
while (true);
  }
if (Serial1.available())  sdisplay_location();
  

} 

void moteuropen()
{
    for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
    myservo.write(pos);    // tell servo to go to position in variable 'pos'
    delay(2);             // waits 15ms for the servo to reach the position
  }
}
void moteurclose(){
  delay(2000);
  for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
    myservo.write(pos);    // tell servo to go to position in variable 'pos'
    delay(2);             // waits 15ms for the servo to reach the position
  }
 }
void display_location()
{
   float x,y;
   BT.print(F("Location:"));
   if (gps.location.isValid())
      {x=gps.location.lat();
      BT.print(x,6);
      BT.print(F(","));
      y=gps.location.lng();
      BT.print(y,6);
        }
       else {BT.print(F("invalid"));}
  BT.print(F(""));
  }
  void sdisplay_location()
{
   float x,y;
   Serial.print(F("Location:"));
      
      x=gps.location.lat();
      Serial.print(x,6);
      Serial.print(F(","));
      y=gps.location.lng();
      Serial.print(y,6);
  
  Serial.print(F(""));
  }
