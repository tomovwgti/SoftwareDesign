/**
 * Created by JetBrains WebStorm.
 * User: tomo
 * Date: 12/02/13
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */

(function (global) {
    // WebSocket
    var ws = new WebSocket('ws://localhost:8001/');

    // サーバに送信
    $(this).keydown(function (key) {
        if (key.keyCode === 13) {
            var msg = $('#send').val();
            console.log('send message --> ' + msg);
            ws.send(msg);
            $('#send').val('');
        }
    });

    // サーバから受信
    ws.onmessage = function (event) {
        var receive_message = JSON.parse(event.data);
        console.log('receive message <-- ' + event.data);
    }
}(this));

