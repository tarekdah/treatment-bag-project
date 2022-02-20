#include <Adafruit_NeoPixel.h>
#include <ArduinoJson.h>
#include <FirebaseArduino.h>
#include <ESP8266WiFi.h>
#define FIREBASE_HOST "projectbag2-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "sRHs8HhvKMemf8Dp8nUvN3M1TysUwYeEiAtNh6kH"
#define WIFI_SSID "Drake"
#define WIFI_PASSWORD "kabat1234"

/*
 * 
 * 
 * 
 * 
 * oledddddddddddddddddd
 * 
 * 
 */

#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels

// Declaration for an SSD1306 display connected to I2C (SDA, SCL pins)
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);





//from arduino
//#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
#include <avr/power.h> // Required for 16 MHz Adafruit Trinket
#endif
#define LED_COUNT 60
#define LED_PIN    D8
#define LED_PIN2    D7

int num=0;
int pin         =  D8;
int numPixels   = 30;
int x=0;
int BUTTON = D4;
int BUTTONstate = 0;
int pixelFormat = NEO_GRB + NEO_KHZ800;
Adafruit_NeoPixel strip(LED_COUNT, LED_PIN, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip2(LED_COUNT, LED_PIN2, NEO_GRB + NEO_KHZ800);

Adafruit_NeoPixel *pixels;
#define DELAYVAL 500

int n = -1;
int push;
#include "ESPBattery.h";

/////////////////////////////////////////////////////////////////

ESPBattery battery = ESPBattery();


void stateHandler(ESPBattery& b) {
  int state = b.getState();
  int level = b.getLevel();

  Serial.print("\nCurrent state: ");
  Serial.println(b.stateToString(state));
  Serial.print("Current percentage: ");
  Serial.println(b.getPercentage());
}



void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  //Serial.setTimeout(2000);
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);   
  }
  //Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  WiFi.setSleepMode(WIFI_NONE_SLEEP);
  //pinMode(D1, OUTPUT);
  //pinMode(D2,INPUT);
  //Serial1.begin(115200);
  delay(500);
 strip.begin();
 strip2.begin();       
 strip.show();
 strip2.show();            
 strip.setBrightness(50);
 strip2.setBrightness(50);
#if defined(__AVR_ATtiny85__) && (F_CPU == 16000000)
  clock_prescale_set(clock_div_1);
#endif
pixels = new Adafruit_NeoPixel(numPixels, pin, pixelFormat);
pixels->begin();
  String s = Firebase.getString("/kids/123456789/Score");
  n=s.toInt();
  Serial.println(n);
  if (Firebase.failed()) {
    Serial.println("nooooooooo");
  }
  
  x=n;
  if (x!=num){
      num=x;
      delay(1000);
      pixels->clear();
      pixels->show();  
    }
delay(100);
  for(int i=0; i<num; i++) {
    if(i<num){
    pixels->setPixelColor(i, pixels->Color(255, 0, 0));
    i++;
    pixels->show();  
    delay(100); 
    }
    if(i<num){
    pixels->setPixelColor(i, pixels->Color(0, 255, 0));
    i++;
    pixels->show();  
    delay(100); 
    }
    if(i<num)
    pixels->setPixelColor(i, pixels->Color(0, 0, 255));{
    pixels->show();  
    delay(100); 
    } // Send the updated pixel colors to the hardware.

  }
    if(num==10){
    //rainbow(10);             
    theaterChaseRainbow(50);
    delay(100);
    }
  
    pixels->clear();
    pixels->show();  
    delay(100);
    pixels->clear();
    pixels->show();  

  if(!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) { // Address 0x3D for 128x64
    Serial.println(F("SSD1306 allocation failed"));
    for(;;);
  }
  delay(100);
 display.clearDisplay();
  display.setTextSize(7);
  display.setTextColor(WHITE);
  display.setCursor(40, 15);
  // Display static text
  display.println(n);
  display.display();
  delay(5000); 
  display.clearDisplay();
  display.display();
  /*battery.setLevelChangedHandler(stateHandler);
  display.setTextSize(2);
  display.setTextColor(WHITE);
  display.setCursor(0,0);
  // Display static text
  display.println(battery.getPercentage());
  display.println("%");
  display.display();
  delay(5000); 
  display.clearDisplay();
  display.display();*/
  Serial.println("\n\nLevel Handler Demo");
  // call the handler once to display the current status
  stateHandler(battery);
  battery.loop();
}

void loop() {
  Serial.println("going to sleep");
  pixels->clear();
  pixels->show(); 
  ESP.deepSleep(0);
  Serial.println("woke up");
}




void theaterChase(uint32_t color, int wait) {
  for(int a=0; a<10; a++) {  // Repeat 10 times...
    for(int b=0; b<3; b++) { //  'b' counts from 0 to 2...
      strip.clear();         //   Set all pixels in RAM to 0 (off)
      // 'c' counts up from 'b' to end of strip in steps of 3...
      for(int c=b; c<strip.numPixels(); c += 3) {
        strip.setPixelColor(c, color); // Set pixel 'c' to value 'color'
      }
      strip.show(); // Update strip with new contents
      delay(wait);  // Pause for a moment
    }
  }
}

void rainbow(int wait) {
  // Hue of first pixel runs 5 complete loops through the color wheel.
  // Color wheel has a range of 65536 but it's OK if we roll over, so
  // just count from 0 to 5*65536. Adding 256 to firstPixelHue each time
  // means we'll make 5*65536/256 = 1280 passes through this loop:
  for(long firstPixelHue = 0; firstPixelHue < 5*65536; firstPixelHue += 256) {
    // strip.rainbow() can take a single argument (first pixel hue) or
    // optionally a few extras: number of rainbow repetitions (default 1),
    // saturation and value (brightness) (both 0-255, similar to the
    // ColorHSV() function, default 255), and a true/false flag for whether
    // to apply gamma correction to provide 'truer' colors (default true).
    strip.rainbow(firstPixelHue);
    // Above line is equivalent to:
    // strip.rainbow(firstPixelHue, 1, 255, 255, true);
    strip.show(); // Update strip with new contents
    delay(wait);  // Pause for a moment
  }
}



void theaterChaseRainbow(int wait) {
  int firstPixelHue = 0;     // First pixel starts at red (hue 0)
  for(int a=0; a<30; a++) {  // Repeat 30 times...
    for(int b=0; b<3; b++) { //  'b' counts from 0 to 2...
      strip2.clear();         //   Set all pixels in RAM to 0 (off)
      // 'c' counts up from 'b' to end of strip in increments of 3...
      for(int c=b; c<strip2.numPixels(); c += 3) {
        // hue of pixel 'c' is offset by an amount to make one full
        // revolution of the color wheel (range 65536) along the length
        // of the strip (strip.numPixels() steps):
        int      hue   = firstPixelHue + c * 65536L / strip2.numPixels();
        uint32_t color = strip2.gamma32(strip2.ColorHSV(hue)); // hue -> RGB
        strip2.setPixelColor(c, color); // Set pixel 'c' to value 'color'
      }
      strip2.show();                // Update strip with new contents
      delay(wait);                 // Pause for a moment
      firstPixelHue += 65536 / 90; // One cycle of color wheel over 90 frames
      strip2.clear();
      strip2.show();
    }
  }
}
