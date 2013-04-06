/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomovwgti.light;

import io.socket.SocketIO;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import org.json.JSONObject;

import com.tomovwgti.android.accessory.io.OutputData;
import com.tomovwgti.json.Light;
import com.tomovwgti.json.Msg;
import com.tomovwgti.json.Value;

public class LedLight extends OutputData {

    static final String TAG = LedLight.class.getSimpleName();

    // ADK device command
    private static final byte LED_COMMAND = 2;

    // ADK device parameter
    private static final byte RED_LED = 0;
    private static final byte GREEN_LED = 1;
    private static final byte BLUE_LED = 2;

    private SocketIO mSocket;
    public int red;
    public int green;
    public int blue;

    public LedLight(SocketIO socket) {
        mSocket = socket;
    }

    @Override
    public void sendData() {
        sendCommand(LED_COMMAND, RED_LED, red);
        sendCommand(LED_COMMAND, GREEN_LED, green);
        sendCommand(LED_COMMAND, BLUE_LED, blue);
    }

    public void sendWebSocket() {
        Msg msg = new Msg();
        Value value = new Value();
        Light light = new Light();
        value.setCommand("light");
        value.setSender("android");
        light.setRed(red);
        light.setGreen(green);
        light.setBlue(blue);
        value.setLight(light);
        msg.setValue(value);

        try {
            mSocket.emit("message", new JSONObject(JSON.encode(msg)));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
