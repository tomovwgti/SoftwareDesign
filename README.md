# Androidエンジニア養成読本 Vol.2
[Androidエンジニア養成読本 Vol.2](http://gihyo.jp/book/2013/978-4-7741-5888-4)用のgitリポジトリです。

## git clone
```
 $ git clone https://github.com/tomovwgti/SoftwareDesign.git
```

#### Software Design 8月号当時のコードは上記に続けて以下で取得できます
```
$ git checkout -b v1.0 refs/tags/v1.0
```
## リポジトリの構成

- Android : Androidプロジェクト
- Arduino : Arduinoプロジェクト
- Node : Node.jsサーバ/クライアント
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
