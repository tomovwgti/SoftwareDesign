/**
 * Created by JetBrains WebStorm.
 * User: tomo
 * Date: 12/02/13
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */

(function (global) {
    // socket.io
    var socket = io.connect();
    var msg = {
        'command': 'Message',
        'message': ''
    };

    // サーバに送信
    $(this).keydown(function (key) {
        if (key.keyCode === 13) {
            msg.message = $('#send').val();
            console.log('send message --> ' + JSON.stringify(msg));
            socket.emit('message', { value: msg });
            $('#send').val('');
        }
    });

    // メッセージを受けた時
    socket.on('message', function (event) {
        var receive_message = event.value;
        console.log('receive message <-- ' + event.value.message);

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
    });
}(this));

