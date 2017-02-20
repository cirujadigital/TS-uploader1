import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import processing.net.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class thingspeak extends PApplet {

/** PARA PROCESSING 2.2.1
 * ENVIO de dos datos desde Arduino a TS v\u00eda puerto SERIE (datos separados por una coma dato1,dato2)
cirujadigital
 */




//CONFIGURACION
  
String lines[]; 

String APIKEY; 

int PORTNUM = 0; //numero de puerto de Arduino
String input ="0";
//FIN CONFIGURACION
  String ln;
 
Serial arduino;
Client c;
String data; //buffer
float number1; //ac\u00e1 se almacena el valor1 
float number2; //idem 2
float number3;

boolean conectado = false;
boolean conectado_ok = false;
public void setup() {
  size(600, 450);
println(Serial.list());
lines= loadStrings("api.txt"); //modificar la apikey en el archivo
APIKEY= trim(lines[0]);   

    
 
}

public void draw() {
  background(0,0,0);
  fill(255);
  text("Arduino Serie a ThinkSpeak- By Ciruja Digital", 10, 20);
 
  if (conectado == false)
  { text("INTRODUZCA EL NOMBRE DEL PUERTO SERIE (default 0) y presione ENTER", 10, 40);
    text(join(Serial.list(),","),10, 60); //Listamos todos los puertos serie.
  text(input, 10, 80);
  }
  if (conectado == true && conectado_ok == false)
  {
    PORTNUM = PApplet.parseInt(input);

  arduino = new Serial(this, Serial.list()[PORTNUM], 9600); //Iniciamos el puerto serie
  conectado_ok = true;
  }
  
 
if (conectado == true && conectado_ok == true)
{
      text(("Puerto seleccionado:"+PORTNUM), 10, 40);
     text(("API KEY: "+APIKEY), 10, 60);
  fill(0, 255, 0);
  text("Dato1:  " + number1, 10, 100);
 text("Dato2:  " + number2, 10, 120);
   text("Dato3:  " + number3, 10, 140);
  if( data != null ) {
    fill(0, 255, 0);
    text("Server Response:", 10, 160);
    fill(200);
    text(data, 10, 180);
  }
  if(c != null) {
    if (c.available() > 0) { 
      data = c.readString(); 
      println(data);
    }
  }



  if( (ln = arduino.readStringUntil('\n')) != null) { //Si hay una nueva linea de arduino, la leemos, la almacenamos, la spliteamos
      String splitst[] = split(ln,',');
    try {
    
      number1 = new Float(trim(splitst[0]));
      number2 =  new Float(trim(splitst[1]));
      number3 =  new Float(trim(splitst[2]));
      if(number1 < 1025 || number2<1025) {
        println("Escribiendo... " + number1);
         println("Escribiendo... " + number2);
             println("Escribiendo... " + number3);
        sendNumber(number1,number2,number3); //Enviamos los valores al server.
      }
    }
    catch(Exception ex) {
    }
  }
}
}

public void sendNumber(float num1,float num2, float num3) {
  c = new Client(this, "api.thingspeak.com", 80); 
 // c.write("POST /update?key="+APIKEY+"&"+FIELD1+"=" + num1 +"&"+FIELD2+"=" + num2+"&"+FIELD3+"=" + num3+ " HTTP/1.1\n");
  c.write("POST /update?key="+APIKEY+"&"+"field1"+"=" + num1 +"&"+"field2"+"=" + num2+"&"+"field3"+"=" + num3+ " HTTP/1.1\n");
 
 c.write("Host: api.thingspeak.com\n\n"); 
}


public void keyPressed() {
  if (keyCode == ENTER ) {
    conectado = true;
  } else if (keyCode == BACKSPACE) {
    input = "";
  } else if (keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
    input = input + key;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "thingspeak" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
