#include <ESP8266WiFi.h>
#include<Firebase_ESP_Client.h>
#include <Servo.h>

#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"

//#define WIFI_SSID ""
//#define WIFI_PASSWORD ""

#define WIFI_SSID "Mahardhika"
#define WIFI_PASSWORD "asdfghjkl"

#define DATABASE_URL "https://smarthomezonaphoto-default-rtdb.firebaseio.com/" // Alamat Firebase
#define API_KEY "AIzaSyCo36ZEYf0zRJ2q7XDrvhG8emA5cz0iV9E"
#define USER_EMAIL "zonaphoto@gmail.com"
#define USER_PASSWORD "tugasakhir"

FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;
#define pagar D2
#define LampuDepan D3
#define LampuStudio D4
#define SensorPir D5
#define SensorLDR A0

Servo myservo;  // create servo object to control a servo
// twelve servo objects can be created on most boards


void setup() {
  Serial.begin(9600);                                 // Pengaktifan Serial

  pinMode(LampuDepan, OUTPUT);
  pinMode(LampuStudio, OUTPUT);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected: ");
  Serial.println(WiFi.SSID());
  Serial.println();

  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

  config.api_key = API_KEY;

  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  config.database_url = DATABASE_URL;

  config.token_status_callback = tokenStatusCallback;
  Firebase.begin(&config, &auth);
  
  myservo.attach(pagar);  // attaches the servo on GIO2 to the servo object
  Firebase.RTDB.setInt(&fbdo, "/Pagar", 0);
  Firebase.RTDB.getInt(&fbdo, "/Mode");
  Firebase.RTDB.getInt(&fbdo, "/LampuDepan");
  Firebase.RTDB.getInt(&fbdo, "/LampuStudio");
  Firebase.reconnectWiFi(true);
}

int FbLampuDepan;
int FbLampuStudio;
int FbCekMode;
int FbPagar;

void loop() {
  
     if(Firebase.RTDB.getInt(&fbdo, "/Pagar")) {
      FbPagar = fbdo.intData();
    }

    if(FbPagar == 180){
        myservo.write(180);
        Serial.println("Pagar Kebuka");
    } else if(FbPagar == 0){  
      myservo.write(1);
        Serial.println("Pagar Ketutup");                            
    }

    int value = digitalRead(SensorPir);

  // Baca Nilai ldr
  int NilaiLdr = analogRead(SensorLDR);
  Serial.print("Nilai Sensor LDR   : ");
  Serial.println(NilaiLdr);

  Firebase.RTDB.setInt(&fbdo, "/ldr", NilaiLdr);              //Mengirim nilai sensor ldr ke firebase
   
        
     //Mengambil Data Dari Firebase
    if(Firebase.RTDB.getInt(&fbdo, "/LampuDepan")) {
       FbLampuDepan = fbdo.intData();
    }
    if(Firebase.RTDB.getInt(&fbdo, "/LampuStudio")) {
      FbLampuStudio = fbdo.intData();
    }
    if(Firebase.RTDB.getInt(&fbdo, "/Mode")) {
      FbCekMode = fbdo.intData();
    }

    if(FbCekMode == 0) {
      Serial.println("Mode Otomatis Aktif");
      
      if(NilaiLdr <= 900){
        digitalWrite(LampuDepan, HIGH);
        Serial.println("Lampu Depan Mati");
        Firebase.RTDB.setString(&fbdo, "/LPDepan", "Mati");
        } else if (NilaiLdr >= 901) {
          digitalWrite(LampuDepan, LOW);
          Serial.println("Lampu Depan Nyala");
          Firebase.RTDB.setString(&fbdo, "/LPDepan", "Nyala");
        }

     if(value == HIGH){
       digitalWrite(LampuStudio, LOW); 
       Serial.println("Lampu Studio Nyala");
       Firebase.RTDB.setString(&fbdo, "/LPStudio", "Nyala");
//     delay(5000);
     } else {
       digitalWrite(LampuStudio, HIGH); 
       Serial.println("Lampu Studio Mati");
       Firebase.RTDB.setString(&fbdo, "/LPStudio", "Mati");
      }
    } else if(FbCekMode == 1){
      Serial.println("Mode Manual Aktif");

      if (FbLampuDepan == 0) {
      digitalWrite(LampuDepan, LOW);
      Serial.println("Lampu Depan Nyala");
      } else if (FbLampuDepan == 1) {
        digitalWrite(LampuDepan, HIGH);
        Serial.println("Lampu Depan Mati");
      }
      
      if (FbLampuStudio == 0) {
      digitalWrite(LampuStudio, LOW);
      Serial.println("Lampu Studio Nyala");
      } else if (FbLampuStudio == 1) {
        digitalWrite(LampuStudio, HIGH);
        Serial.println("Lampu Studio Mati");
      }
    }

    Serial.println();
   
}
