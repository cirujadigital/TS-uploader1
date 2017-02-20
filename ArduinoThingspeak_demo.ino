//Arduino to Thingspeak by USB.
//In order to compile and load this example program, you'll need the Adafruit BMP085 librar

y and SimpleDHT11 library. 
#include <Wire.h>
#include <Adafruit_BMP085.h>

#include <SimpleDHT.h>

int pinDHT11 = 10;
SimpleDHT11 dht11;

#define intervalo 20000
 

Adafruit_BMP085 bmp;

void setup() {
Serial.begin(9600);
bmp.begin();
delay(300);

}


void loop() {
   byte t_dht=0;
 byte h = 0;
 
if (dht11.read(pinDHT11, &t_dht, &h, NULL)) {
    
    return;
  }
  
 float t = bmp.readTemperature();
 float pres = bmp.readPressure();

Serial.print(t); //reemplazar por el valor a enviar
Serial.print(',');
Serial.print((int)h);
Serial.print(',');
Serial.print(pres);// reemplazar por el valor a enviar
Serial.println(",,,,,"); //Field3,field4,field5,field6,field7,field8 Si se envían otros field, desglosar el envío de serial como en las lineas de arriba.
delay(intervalo); //freq envío.
}
