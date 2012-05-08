
package com.tomovwgti.light;

import java.net.URI;
import java.net.URISyntaxException;

import android.util.Log;
import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;

public class WebSocketManager {
    static final String TAG = WebSocketManager.class.getSimpleName();
    private static WebSocket websocket;

    public static void connect(String url, WebSocketEventHandler handler) {

        try {
            URI uri = new URI(url);
            websocket = new WebSocketConnection(uri);

            Log.d(TAG, "websocket connect start");
            websocket.setEventHandler(handler);
            websocket.connect();

        } catch (WebSocketException wse) {
            wse.printStackTrace();
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
    }

    public static void send(String message) {

        try {
            Log.d(TAG, "websocket connect send");
            websocket.send(message);
        } catch (WebSocketException wse) {
            wse.printStackTrace();
        }
    }

    public static void close() {
        try {
            Log.d(TAG, "websocket connect close");
            websocket.close();
        } catch (WebSocketException wse) {
            wse.printStackTrace();
        }
    }
}
