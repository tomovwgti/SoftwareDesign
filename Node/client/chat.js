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
    var msg = {
        'command': 'Message',
        'message': ''
    };

    // サーバに送信
    $(this).keydown(function (key) {
        if (key.keyCode === 13) {
            msg.message = $('#send').val();
            console.log('send message --> ' + JSON.stringify(msg));
            ws.send(JSON.stringify(msg));
            $('#send').val('');
        }
    });

    // サーバから受信
    ws.onmessage = function (event) {
        var receive_message = JSON.parse(event.data);
        console.log('receive message <-- ' + event.data);

        // Message
        switch (receive_message.message) {
            // （」・ω・）」うー！
            case 'uu':
                $('body').append('<div> （」・ω・）」うー！ </div>');
                break;
            // （／・ω・）／にゃー！
            case 'nyaa':
                $('body').append('<div> （／・ω・）／にゃー！ </div>');
                break;
            default:
                $('body').append('<div>' + receive_message.message + '</div>');
        }
    }
}(this));

