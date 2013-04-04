
package com.tomovwgti.socketio;

import net.arnx.jsonic.JSON;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomovwgti.json.Geo;
import com.tomovwgti.json.Msg;
import com.tomovwgti.json.Value;
import com.tomovwgti.socketio.SocketIOFragment.MessageCallback;
import com.tomovwgti.socketio.SocketIOFragment.MessageCallbackPicker;

public class SocketIOActivity extends FragmentActivity implements MessageCallbackPicker,
        MessageCallback {
    static final String TAG = SocketIOActivity.class.getSimpleName();

    private static final String PREF_KEY = "IPADDRESS";
    private AlertDialog mAlertDialog;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private Handler handler = new Handler();

    private static final String UU_STR = "uu";
    private static final String NYAA_STR = "nyaa";

    private static final String UU_TEXT = "(」・ω・)」うー！";
    private static final String NYAA_TEXT = "(／・ω・)／にゃー！";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 起動時にキーボードが開かないように
        this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.message);

        { // SocketIOFragmentの作成と登録
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(new SocketIOFragment(), "socketio");
            transaction.commit();
        }

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPref.edit();

        // うーボタン押下時の挙動
        Button uuBtn = (Button) findViewById(R.id.btn_uu_btn);
        uuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg sendMessage = new Msg();
                Value value = new Value();
                value.setCommand("");
                value.setSender("android");
                value.setCommand("Message");
                value.setMessage(UU_STR);
                sendMessage.setValue(value);
                ((SocketIOFragment) getFragment()).emit(sendMessage);
                setMessage(UU_TEXT, Color.GREEN);
            }
        });

        // にゃーボタン押下時の挙動
        Button nyaaBtn = (Button) findViewById(R.id.btn_nyaa_btn);
        nyaaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg sendMessage = new Msg();
                Value value = new Value();
                value.setCommand("");
                value.setSender("android");
                value.setCommand("Message");
                value.setMessage(NYAA_STR);
                sendMessage.setValue(value);
                ((SocketIOFragment) getFragment()).emit(sendMessage);
                setMessage(NYAA_TEXT, Color.GREEN);
            }
        });

        // Mapボタン押下時の挙動
        Button mapBtn = (Button) findViewById(R.id.btn_map_btn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg sendMessage = new Msg();
                Value value = new Value();
                value.setCommand("geo");
                value.setMessage("Map");
                value.setSender("android");
                Geo geo = new Geo();
                geo.setLat("36.744386");
                geo.setLon("139.457703");
                value.setGeo(geo);
                sendMessage.setValue(value);
                ((SocketIOFragment) getFragment()).emit(sendMessage);
                setMessage(JSON.encode(sendMessage), Color.GREEN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPref.getString(PREF_KEY, "").equals("")) {
            // IPアドレス確認ダイアログ
            mAlertDialog = showAlertDialog();
            mAlertDialog.show();
        } else {
            ((SocketIOFragment) getFragment()).connectSocketIO(mPref.getString(PREF_KEY, ""));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((SocketIOFragment) getFragment()).disconnectSocketIO();
    }

    private void setMessage(final String message, final int color) {
        // WebSocketHandlerのonMessageは別スレッドなのでhandlerを用いてviewの書き換えを行う
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView messageArea = (TextView) findViewById(R.id.message_area);
                messageArea.setText(message);
                messageArea.setTextColor(color);
            }
        });
    }

    @Override
    public MessageCallback getInstance() {
        return this;
    }

    private Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag("socketio");
    }

    public AlertDialog showAlertDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View entryView = factory.inflate(R.layout.dialog_entry, null);
        final EditText edit = (EditText) entryView.findViewById(R.id.ip_address);

        if (mPref.getString(PREF_KEY, "").equals("")) {
            edit.setHint("***.***.***.***");
        } else {
            edit.setText(mPref.getString(PREF_KEY, ""));
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
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                                    "socketio");
                            ((SocketIOFragment) fragment).connectSocketIO(editStr);
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
                        mEditor.putString(PREF_KEY, editStr);
                        mEditor.commit();
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                                "socketio");
                        ((SocketIOFragment) fragment).connectSocketIO(editStr);
                    }
                }).create();
    }

    /**
     * サーバからのSocket.IOイベント／メッセージ
     */
    @Override
    public void onDisconnect() {
        Toast.makeText(SocketIOActivity.this, "Disconnect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnect() {
        Toast.makeText(SocketIOActivity.this, "Connect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHertbeat() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMessage() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onJsonMessage(Msg message) {
        Log.d(TAG, "Socket.io message");
        Value value = message.getValue();
        if (value.getCommand() != null && value.getCommand().equals("")) {
            if (UU_STR.equals(value.getMessage())) {
                setMessage(UU_TEXT, Color.RED);
            } else if (NYAA_STR.equals(value.getMessage())) {
                setMessage(NYAA_TEXT, Color.RED);
            } else {
                setMessage(value.getMessage(), Color.BLUE);
            }
        } else if (value.getCommand() != null && value.getCommand().equals("geo")) {
            // Map呼び出し
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:" + value.getGeo().getLat() + ","
                    + value.getGeo().getLon() + "?z=13"));
            startActivity(intent);
        } else if (value.getCommand() != null && value.getCommand().equals("http")) {
            // Browser呼び出し
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(value.getMessage()));
            startActivity(intent);
        } else {
            // それ以外
            setMessage(value.getMessage(), Color.GREEN);
        }
    }

    @Override
    public void onEvent() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError() {
        Toast.makeText(SocketIOActivity.this, "Socket.IO Error!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAck() {
        // TODO Auto-generated method stub

    }
}
