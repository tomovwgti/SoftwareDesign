/**
 * Created with JetBrains WebStorm.
 * User: tomo
 * Date: 12/04/29
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */

/* JSONの形式は以下のようになっている
{
    "sender":"browser",
    "command":"led",
    "led":{
        "status":false
    },
    "light":{
        "red":255,
        "green":255,
        "blue":255
    },
    "geo":{
        "lat":36.744386,
        "lon":139.457703
    }
}
*/

$(function () {
    // socket.io
    var socket = io.connect();
    var led_state = false;

    $('#chime').click(function() {
        var msg = {
            'sender': 'browser',
            'command': 'chime'
        };
        console.log(JSON.stringify(msg));
        socket.emit('message', { value: msg });
    });

    $('#led').click(function() {
        led_state = !led_state;
        var msg = {
            'sender': 'browser',
            'command': 'led',
            'led' : {
                'status': false
            }
        };
        msg.led.status = led_state;
        console.log(JSON.stringify(msg));
        socket.emit('message', { value: msg });
    });

    // Message from Server
    socket.on('message', function (event) {
        var receive_message = event.value;
        console.log('receive message <-- ' + JSON.stringify(event.value));

        // Command
        switch (receive_message.command) {
            case 'geo':
                var map = 'http://maps.google.co.jp/?ie=UTF8&ll='
                    + receive_message.geo.lat +','
                    + receive_message.geo.lon + '&z=13';
                document.location = map;
                break;
            case 'light':
                $('.jquery-ui-slider-red-value').val(receive_message.light.red);
                $('.jquery-ui-slider-green-value').val(receive_message.light.green);
                $('.jquery-ui-slider-blue-value').val(receive_message.light.blue);
                $('#jquery-ui-slider-red').slider('value', receive_message.light.red);
                $('#jquery-ui-slider-green').slider('value', receive_message.light.green);
                $('#jquery-ui-slider-blue').slider('value', receive_message.light.blue);
                break;
        }

        // Message
        switch (receive_message.message) {
            // （」・ω・）」うー！
            case 'uu':
                $('#uu').vtoggle();
                if ($('#uu').css('visibility') === 'visible') {
                    uu_sound.play();
                }
                break;
            // （／・ω・）／にゃー！
            case 'nyaa':
                $('#nyaa').vtoggle();
                if ($('#nyaa').css('visibility') === 'visible') {
                    nyaa_sound.play();
                }
                break;
        }
    });

    $(function() {
        var msg = {
            'sender': 'browser',
            'command': 'light',
            'light' : {
                'red': 0,
                'green': 0,
                'blue': 0
            }
        };

        $('#jquery-ui-slider > div > .jquery-ui-slider-multi').each(function() {

            var value = parseInt($(this).text(),10);
            var inputValue = '.' + $(this).attr('id') + '-value';
            $(this).empty().slider( {
                value: value,
                range: 'min',
                min: 0,
                max: 255,
                animate: true,
                slide: function( event, ui ) {
                    $(inputValue).val(ui.value);
                    $(inputValue).html(ui.value);

                    if (inputValue === '.jquery-ui-slider-red-value') {
                        msg.light.red = ui.value;
                        msg.light.green = $('.jquery-ui-slider-green-value').val();
                        msg.light.blue = $('.jquery-ui-slider-blue-value').val();
                    } else if (inputValue === '.jquery-ui-slider-green-value') {
                        msg.light.red = $('.jquery-ui-slider-red-value').val();
                        msg.light.green = ui.value;
                        msg.light.blue = $('.jquery-ui-slider-blue-value').val();
                    } else {
                        msg.light.red = $('.jquery-ui-slider-red-value').val();
                        msg.light.green = $('.jquery-ui-slider-green-value').val();
                        msg.light.blue = ui.value;
                    }

                    console.log(JSON.stringify(msg));
                    socket.emit('message', { value: msg });
                }
            } );
            $(inputValue).val($(this).slider('value'));
            $(inputValue).html($(this).slider('value'));
        } );
    } );
});