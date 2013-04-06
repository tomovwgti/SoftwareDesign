# Software Design 8月号
Software Design用のgitリポジトリです。

## git clone
```
 $ git clone git://github.com/tomovwgti/SoftwareDesign.git
```

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

#### [SocketIO](https://github.com/tomovwgti/SoftwareDesign/tree/master/Android/SocketIO)
- Socket.IOを使ったAndroidクライアント


### [Arduino](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino)
***
#### [Device](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino/Device)
- ADKでArduinoシールドに載ったLEDとフルカラーLEDをコントロールする

#### [FullColorLED](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino/FullColorLED)
- Arduino単体でフルカラーLEDをコントロールするサンプル

#### [LED](https://github.com/tomovwgti/SoftwareDesign/tree/master/Arduino/LED)
- ADKでLEDをコントロールする

### [Node](https://github.com/tomovwgti/SoftwareDesign/tree/master/Node)
***
#### [chatserver](https://github.com/tomovwgti/SoftwareDesign/tree/master/Node/chatserver)
- Socket.IOを使ってNode.jsサーバとのやりとりを確認するチャットサーバ/クライアント

#### [webcommand](https://github.com/tomovwgti/SoftwareDesign/tree/master/Node/webcommand)
- ブラウザでLEDをコントロールするためのサーバ/クライアント

***

## NVM( Node Version Manager)とNode.jsのインストール
```
 $ git clone https://github.com/creationix/nvm.git .nvm
 $ source .nvm/nvm.sh
 $ nvm install v0.8.16  // Node.js v0.8.16をインストール
 $ nvm use v0.8.16      // Node v0.8.16を使用
```

## ADK Libraryのクラス図
[クラス図](https://github.com/tomovwgti/SoftwareDesign/blob/master/class.png)