var ws = require('websocket-server'); 
var sockets = [];
  
var server = ws.createServer();

// サーバの開始
server.addListener('listening', function(nonnection) {
    console.log('listening..');
});

// 接続された際のリスナー
server.addListener('connection', function(connection){
    sockets.push(connection);
    console.log('connect');

    connection.addListener('message', function(message) {
        console.log(message);
        for (var k in sockets) {
            if (connection != sockets[k]){
                sockets[k].send(message);
            }
        }
    });
});
    
// 接続が切断された際のリスナー
server.addListener('close', function(connection){
    console.log('close');
});

// listenするポート番号を指定
server.listen(8001);
