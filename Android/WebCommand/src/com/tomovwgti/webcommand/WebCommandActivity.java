
package com.tomovwgti.webcommand;

import net.arnx.jsonic.JSON;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tomovwgti.android.accessory.AccessoryBaseActivity;
import com.tomovwgti.json.Msg;

import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

public class WebCommandActivity extends AccessoryBaseActivity {
    static final String TAG = WebCommandActivity.class.getSimpleName();

    private static String WS_URI = "ws://192.168.110.110:8001/";

    private static final String UU_STR = "uu";
    private static final String NYAA_STR = "nyaa";

    private static final String UU_TEXT = "(」・ω・)」うー！";
    private static final String NYAA_TEXT = "(／・ω・)／にゃー！";

    private Handler handler = new Handler();
    private Activity activity;
    private AlertDialog mAlertDialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SoundPool mSp;
    private int mId;

    @Override
    protected void showControls() {
        setContentView(R.layout.message);
        activity = this;

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        // IPアドレス確認ダイアログ
        mAlertDialog = showAlertDialog();
        mAlertDialog.show();

        // チャイム音準備
        mSp = new SoundPool(1, AudioManager.STREAM_RING, 0);
        mId = mSp.load(this, R.raw.chime, 1);

        // うーボタン押下時の挙動
        Button uuBtn = (Button) findViewById(R.id.btn_uu_btn);
        uuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg msg = new Msg();
                msg.setCommand("");
                msg.setSender("android");
                msg.setMessage(UU_STR);
                String message = JSON.encode(msg);
                WebSocketManager.send(message);
                setMessage(UU_TEXT, Color.GREEN);
            }
        });

        // にゃーボタン押下時の挙動
        Button nyaaBtn = (Button) findViewById(R.id.btn_nyaa_btn);
        nyaaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg msg = new Msg();
                msg.setCommand("");
                msg.setSender("android");
                msg.setMessage(NYAA_STR);
                String message = JSON.encode(msg);
                WebSocketManager.send(message);
                setMessage(NYAA_TEXT, Color.GREEN);
            }
        });

        // Mapボタン押下時の挙動
        Button mapBtn = (Button) findViewById(R.id.btn_map_btn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg msg = new Msg();
                msg.setCommand("geo");
                msg.setSender("android");
                msg.setLat("36.744386");
                msg.setLon("139.457703");
                String message = JSON.encode(msg);
                WebSocketManager.send(message);
                setMessage(message, Color.GREEN);
            }
        });
    }

    private void setMessage(final String message, final int color) {
        // WebSocketHandlerのonMessageは別スレッドなのでhandlerを用いてviewの書き換えを行う
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView messageArea = (TextView) activity.findViewById(R.id.message_area);
                messageArea.setText(message);
                messageArea.setTextColor(color);
            }
        });
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        WebSocketManager.close();
        return super.clone();
    }

    private void connectWebSocket() {
        Log.i(TAG, "connect start");
        // WebSocket通信開始
        WebSocketManager.connect(WS_URI, new WebSocketEventHandler() {

            @Override
            public void onOpen() {
                Log.d(TAG, "websocket connect open");
            }

            @Override
            public void onMessage(WebSocketMessage message) {
                Log.d(TAG, "websocket message");
                String str = message.getText();
                Msg msg = JSON.decode(str, Msg.class);
                if (msg.getCommand().equals("")) {
                    if (UU_STR.equals(msg.getMessage())) {
                        setMessage(UU_TEXT, Color.RED);
                    } else if (NYAA_STR.equals(msg.getMessage())) {
                        setMessage(NYAA_TEXT, Color.RED);
                    } else {
                        setMessage(str, Color.BLUE);
                    }
                } else if (msg.getCommand().equals("geo")) {
                    // Map呼び出し
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:" + msg.getLat() + "," + msg.getLon() + "?z=13"));
                    startActivity(intent);
                } else if (msg.getCommand().equals("http")) {
                    // Browser呼び出し
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(str));
                    startActivity(intent);
                } else if (msg.getCommand().equals("light")) {
                    // Fill Color LEDコントロール
                    executeLight(msg);
                } else if (msg.getCommand().equals("led")) {
                    // LEDコントロール
                    executeLed(msg);
                } else if (msg.getCommand().equals("chime")) {
                    // Chimeコントロール
                    executeChime(msg);
                } else {
                    // それ以外
                    setMessage(str, Color.GREEN);
                }
            }

            @Override
            public void onClose() {
                Log.d(TAG, "websocket connect close");
            }
        });
    }

    /**
     * Lightコマンドを受けた時の処理
     */
    private void executeLight(Msg msg) {
        // ADKへ出力
        LedLight light = new LedLight();
        light.red = contains(msg.getRed());
        light.green = contains(msg.getGreen());
        light.blue = contains(msg.getBlue());
        light.sendData();
    }

    /**
     * LEDコマンドを受けた時の処理
     */
    private void executeLed(Msg msg) {
        Led led = new Led();
        if (msg.isStatus() == false) {
            led.light = 0;
        } else {
            led.light = 1;
        }
        led.sendData();
    }

    /**
     * CHIMEコマンドを受けた時の処理
     */
    private void executeChime(Msg msg) {
        // Androidで音を鳴らす
        mSp.play(mId, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    private int contains(int value) {
        if (value < 0) {
            return 0;
        } else if (value > 255) {
            return 255;
        }
        return value;
    }

    private AlertDialog showAlertDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View entryView = factory.inflate(R.layout.dialog_entry, null);
        final EditText edit = (EditText) entryView.findViewById(R.id.username_edit);

        if (pref.getString("IPADDRESS", "").equals("")) {
            edit.setHint("***.***.***.***");
        } else {
            edit.setText(pref.getString("IPADDRESS", ""));
        }
        // キーハンドリング
        edit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Enterキーハンドリング
                if (KeyEvent.KEYCODE_ENTER == keyCode) {
                    // 押したときに改行を挿入防止処理
                    if (KeyEvent.ACTION_DOWN == event.getAction()) {
                        return true;
                    }
                    // 離したときにダイアログ上の[OK]処理を実行
                    else if (KeyEvent.ACTION_UP == event.getAction()) {
                        if (edit != null && edit.length() != 0) {
                            // ここで[OK]が押されたときと同じ処理をさせます
                            String editStr = edit.getText().toString();
                            // OKボタン押下時のハンドリング
                            Log.v(TAG, editStr);
                            connectWebSocket();

                            // AlertDialogを閉じます
                            mAlertDialog.dismiss();
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        // AlertDialog作成
        return new AlertDialog.Builder(this).setTitle("Server IP Address").setView(entryView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String editStr = edit.getText().toString();
                        // OKボタン押下時のハンドリング
                        Log.v(TAG, editStr);
                        editor.putString("IPADDRESS", editStr);
                        editor.commit();
                        WS_URI = "ws://" + editStr + ":8001/";
                        connectWebSocket();
                    }
                }).create();
    }
}
