
/**
 * Module dependencies.
 */

var express = require('express')
    , routes = require('./routes')
    , path = require('path');

var app = express()
    , http = require('http')
    , server = http.createServer(app)
    , io = require('socket.io').listen(server); // Socket.io

app.configure(function(){
    app.set('port', process.env.PORT || 3000); // listenするポート番号を指定
    app.set('views', __dirname + '/views');
    app.set('view engine', 'ejs');
    app.use(express.favicon());
    app.use(express.logger('dev'));
    app.use(express.bodyParser());
    app.use(express.methodOverride());
    app.use(app.router);
    app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function(){
    app.use(express.errorHandler());
});

app.get('/', routes.index);

// ポートをリッスンする
server.listen(app.get('port'));
console.log('Listening...');

// クライアントが接続してきたときの処理
io.sockets.on('connection', function(socket) {
    console.log("connect");
    // メッセージを受けた時の処理
    socket.on('message', function(data) {
        // 接続しているクライアントに全てに送信
        console.log("message: " + JSON.stringify(data.value));
        socket.broadcast.emit('message', { value: data.value });
    });

    // クライアントが切断した時に処理
    socket.on('disconnection', function() {
        console.log("disconnect");
    });
});