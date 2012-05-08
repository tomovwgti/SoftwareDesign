/*
  FullColorLED

  フルカラーLEDの色を変える
 */

#define LED_RED    9
#define LED_BLUE  10
#define LED_GREEN 11

void setup() {
  pinMode(LED_RED, OUTPUT);     // 赤LED接続のピン(9番)をアナログ出力に設定
  pinMode(LED_BLUE, OUTPUT);     // 青LED接続のピン(10番)をアナログ出力に設定
  pinMode(LED_GREEN, OUTPUT);     // 緑LED接続のピン(11番)をアナログ出力に設定
  analogWrite(LED_RED, 0);
  analogWrite(LED_BLUE, 0);
  analogWrite(LED_GREEN, 0);
}

void loop() {
  int i = 0;
  while ( i < 100 ) {
    analogWrite(LED_RED, i);  // 赤LEDを点灯
    analogWrite(LED_BLUE, 0);
    analogWrite(LED_GREEN, 0);
    delay(300);
  
    analogWrite(LED_RED, 0);
    analogWrite(LED_BLUE, 0);
    analogWrite(LED_GREEN, i);  // 緑LEDを点灯
    delay(300);

    analogWrite(LED_RED, 0);
    analogWrite(LED_BLUE, i);  // 青LEDを点灯
    analogWrite(LED_GREEN, 0);
    delay(300);
    i+= 10;
  }
}
