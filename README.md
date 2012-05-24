# Software Design
Software Design用のgitリポジトリです。
***
## リポジトリの構成
---
- Android : Androidプロジェクト
- Arduino : Arduinoプロジェクト
- Client : webcommandクライアント
- Node : Node.js
- READEME.md : このファイル

## 各フォルダの説明
---
### Android
***
#### AdkLed
- ADKでオンボードのLEDを点灯させる
- 要AdkLib

#### AdkLib
- AndroidでADKを利用するためのライブラリ

#### LightControl
- Arduino上のフルカラーLEDをスライダーでコントロールする
- 要AdkLib

#### WebCommand
- 

#### WebSocket
- WebSocketを使ったAndroidクライアント


### Arduino
***
#### Device
- ADKでArduinoシールドに載ったLEDとフルカラーLEDをコントロールする

#### FullColorLED
- Arduino単体でフルカラーLEDをコントロールするサンプル

#### LED
- ADKでLEDをコントロールする

### Client
***
- ADKを通してArduinoシールドのフルカラーLEDを操作できるブラウザクライアント

### Node
***
#### client
- WebSocketを使ってNode.jsサーバとのやりとりを確認するブラウザクライアント

#### server
- Node.jsサーバ
***
## NVM/Node.jsのインストール
```
$ git clone https://github.com/creationix/nvm.git .nvm
$ source .nvm/nvm.sh
$ nvm install v0.6.6  // Node.js v0.6.6をインストール
$ nvm use v0.6.6
```