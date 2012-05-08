/**
 * Created with JetBrains WebStorm.
 * User: tomo
 * Date: 12/04/29
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */

$(function () {
    // WebSocket
    var ws = new WebSocket('ws://192.168.110.195:8001/');
    var led_state = false;

    $('#chime').click(function() {
        var msg = {
            'sender': 'browser',
            'command': 'chime'
        }
        console.log(JSON.stringify(msg));
        ws.send(JSON.stringify(msg));
    });

    $('#led').click(function() {
        led_state = !led_state;
        var msg = {
            'sender': 'browser',
            'command': 'led',
            'status': false
        }
        msg.status = led_state;
        console.log(JSON.stringify(msg));
        ws.send(JSON.stringify(msg));
    });

    // Message from Server
    ws.onmessage = function (event) {
        var receive_message = JSON.parse(event.data);
        console.log('receive message <-- ' + event.data);

        // Command
        switch (receive_message.command) {
            case 'geo':
                var map = 'http://maps.google.co.jp/?ie=UTF8&ll=' + receive_message.lat +',' + receive_message.lon + '&z=13'
                document.location = map;
                break;
            case 'light':
                $('.jquery-ui-slider-red-value').val(receive_message.red);
                $('.jquery-ui-slider-green-value').val(receive_message.green);
                $('.jquery-ui-slider-blue-value').val(receive_message.blue);
                $('#jquery-ui-slider-red').slider('value', receive_message.red);
                $('#jquery-ui-slider-green').slider('value', receive_message.green);
                $('#jquery-ui-slider-blue').slider('value', receive_message.blue);
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
    }

    $(function() {
        var msg = {
            'sender': 'browser',
            'command': 'light',
            'red': 0,
            'green': 0,
            'blue': 0
        }

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
                        msg.red = ui.value;
                        msg.green = $('.jquery-ui-slider-green-value').val();
                        msg.blue = $('.jquery-ui-slider-blue-value').val();
                    } else if (inputValue === '.jquery-ui-slider-green-value') {
                        msg.red = $('.jquery-ui-slider-red-value').val();
                        msg.green = ui.value;
                        msg.blue = $('.jquery-ui-slider-blue-value').val();
                    } else {
                        msg.red = $('.jquery-ui-slider-red-value').val();
                        msg.green = $('.jquery-ui-slider-green-value').val();
                        msg.blue = ui.value;
                    }

                    console.log(JSON.stringify(msg));
                    ws.send(JSON.stringify(msg));
                }
            } );
            $(inputValue).val($(this).slider('value'));
            $(inputValue).html($(this).slider('value'));
        } );
    } );
});