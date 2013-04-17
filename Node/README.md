## サーバの作り方

例）chatserverの場合


#### expressのインストール

```
$ sudo npm install express -g
```

#### ひな形の作成とSocket.IOのインストール
```$ express -e chatserver
   create : chatserver
   create : chatserver/package.json
   create : chatserver/app.js
   create : chatserver/public
   create : chatserver/public/javascripts
   create : chatserver/public/images
   create : chatserver/public/stylesheets
   create : chatserver/public/stylesheets/style.css
   create : chatserver/routes
   create : chatserver/routes/index.js
   create : chatserver/routes/user.js
   create : chatserver/views
   create : chatserver/views/index.ejs

   install dependencies:
     $ cd chatserver && npm install

   run the app:
     $ node app

$ cd chatserver && npm install
$ npm install socket.io
```ひな形を起動するには

```$ node app.js
```