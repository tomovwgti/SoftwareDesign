#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

#define LED       13
#define LED_RED    9
#define LED_BLUE  10
#define LED_GREEN 11

AndroidAccessory acc("Google, Inc.",
		     "DemoKit",
		     "DemoKit Arduino Board",
		     "1.0",
		     "http://www.android.com",
		     "0000000012345678");

void setup();
void loop();

void setup()
{
  Serial.begin(9600);
  Serial.println("Start");

  pinMode(LED, OUTPUT);
  pinMode(LED_RED, OUTPUT);     // 赤LED接続のピン(9番)をアナログ出力に設定
  pinMode(LED_BLUE, OUTPUT);     // 青LED接続のピン(10番)をアナログ出力に設定
  pinMode(LED_GREEN, OUTPUT);     // 緑LED接続のピン(11番)をアナログ出力に設定
  analogWrite(LED_RED, 0);
  analogWrite(LED_BLUE, 0);
  analogWrite(LED_GREEN, 0);

  acc.powerOn();
}

void loop()
{
  byte msg[3];

  if (acc.isConnected()) {
                
    int len = acc.read(msg, sizeof(msg), 1);

    if (len > 0) {

      // msg[0] : Command
      // msg[1] : Target
      // msg[2] : Value
/*
      Serial.print("len: ");
      Serial.println(len);
      Serial.print("msg[0]: ");
      Serial.println(msg[0]);
      Serial.print("msg[1]: ");
      Serial.println(msg[1]);
      Serial.print("msg[2]: ");
      Serial.println(msg[2]);
*/
      if (msg[0] == 0x02) {
        switch (msg[1]) {
          case 0:
            analogWrite(LED_RED, msg[2]);
            break;
          case 1:
            analogWrite(LED_GREEN, msg[2]);
            break;
          case 2:
            analogWrite(LED_BLUE, msg[2]);
            break;
        }
      }
      
      if (msg[0] == 0x03) {
        if (msg[2] == 0x00) {
          digitalWrite(LED, LOW);
        } else {
          Serial.println("HIGH");
          digitalWrite(LED, HIGH);
        }
      }
    }
  }
  delay(10);
}
