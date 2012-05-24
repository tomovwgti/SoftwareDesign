# Software Design 8月号
Software Design用のgitリポジトリです。

## リポジトリの構成

- Android : Androidプロジェクト
- Arduino : Arduinoプロジェクト
- Client : webcommandクライアント
- Node : Node.js
- READEME.md : このファイル

## 各フォルダの説明
### [Android](https://github.com/tomovwgti/SoftwareDesign/tree/master/Android)
***
#### [AdkLed](https://github.com/tomovwgti/SoftwareDesign/tree/master/Android/AdkLed)
- ADKでオンボードのLEDを点灯させる
- 要AdkLib

#### [AdkLib](https://github.com/tomovwgti/SoftwareDesign/tree/master/Android/AdkLib)
- AndroidでADKを利用するためのライブラリ

#### [LightControl](https://github.com/tomovwgti/SoftwareDesign/tree/master/Android/LightControl)
- Arduino上のフルカラーLEDをスライダーでコントロールする
- 要AdkLib

#### [WebSocket](https://github.com/tomovwgti/SoftwareDesign/tree/master/Android/WebSocket)
- WebSocketを使ったAndroidクライアント


### [Arduino](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino)
***
#### [Device](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino/Device)
- ADKでArduinoシールドに載ったLEDとフルカラーLEDをコントロールする

#### [FullColorLED](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino/FullColorLED)
- Arduino単体でフルカラーLEDをコントロールするサンプル

#### [LED](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino/LED)
- ADKでLEDをコントロールする

### [Client](https://github.com/tomovwgti/SoftwareDesign/tree/master/Client)
***
- ADKを通してArduinoシールドのフルカラーLEDを操作できるブラウザクライアント

### [Node](https://github.com/tomovwgti/SoftwareDesign/tree/master/Node)
***
#### [client](https://github.com/tomovwgti/SoftwareDesign/tree/master/Node/client)
- WebSocketを使ってNode.jsサーバとのやりとりを確認するブラウザクライアント

#### [server](https://github.com/tomovwgti/SoftwareDesign/tree/master/Node/server)
- Node.jsサーバ

***

## NVM( Node Version Manager)とNode.jsのインストール
```
 $ git clone https://github.com/creationix/nvm.git .nvm
 $ source .nvm/nvm.sh
 $ nvm install v0.6.6  // Node.js v0.6.6をインストール
 $ nvm use v0.6.6      // Node v0.6.6を使用
```

### ADK Libraryのクラス図
[クラス図](class.png)