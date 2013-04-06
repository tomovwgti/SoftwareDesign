
package com.tomovwgti.light;

import io.socket.SocketIO;
import io.socket.util.SocketIOManager;
import net.arnx.jsonic.JSON;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tomovwgti.android.accessory.AccessoryBaseActivity;
import com.tomovwgti.json.Msg;
import com.tomovwgti.json.Value;

public class LightControlActivity extends AccessoryBaseActivity {
    static final String TAG = LightControlActivity.class.getSimpleName();

    private static final String PREF_KEY = "IPADDRESS";
    private AlertDialog mAlertDialog;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private SocketIOManager mSocketManager;
    private SocketIO mSocket;

    private SeekBar mRedLed;
    private SeekBar mGreenLed;
    private SeekBar mBlueLed;
    private TextView mRedText;
    private TextView mGreenText;
    private TextView mBlueText;

    private LedLight mLed = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SocketIOManager.SOCKETIO_DISCONNECT:
                    Log.i(TAG, "SOCKETIO_DISCONNECT");
                    Toast.makeText(LightControlActivity.this, "Disconnect", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SocketIOManager.SOCKETIO_CONNECT:
                    Log.i(TAG, "SOCKETIO_CONNECT");
                    Toast.makeText(LightControlActivity.this, "Connect", Toast.LENGTH_SHORT).show();
                    break;
                case SocketIOManager.SOCKETIO_HERTBEAT:
                    Log.i(TAG, "SOCKETIO_HERTBEAT");
                    break;
                case SocketIOManager.SOCKETIO_MESSAGE:
                    Log.i(TAG, "SOCKETIO_MESSAGE");
                    break;
                case SocketIOManager.SOCKETIO_JSON_MESSAGE:
                    Log.i(TAG, "SOCKETIO_JSON_MESSAGE");
                    Msg message = JSON.decode((String) (msg.obj), Msg.class);
                    Value value = message.getValue();
                    if (value.getCommand().equals("light")) {
                        executeCommand(value);
                    }
                    break;
                case SocketIOManager.SOCKETIO_EVENT:
                    Log.i(TAG, "SOCKETIO_EVENT");
                    break;
                case SocketIOManager.SOCKETIO_ERROR:
                    Log.i(TAG, "SOCKETIO_ERROR");
                    break;
                case SocketIOManager.SOCKETIO_ACK:
                    Log.i(TAG, "SOCKETIO_ACK");
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mSocketManager.disconnect();
    }

    @Override
    protected void showControls() {
        setContentView(R.layout.main);

        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPref.edit();

        // Socket.IO Manager
        mSocketManager = new SocketIOManager(mHandler);

        // IPアドレス確認ダイアログ
        mAlertDialog = showAlertDialog();
        mAlertDialog.show();

        mRedText = (TextView) findViewById(R.id.red);
        mRedLed = (SeekBar) findViewById(R.id.led_red);
        mRedLed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRedText.setText("RED : " + progress);
                if (mLed == null) {
                    mLed = new LedLight(mSocket);
                }
                mLed.red = progress;
                mLed.sendData();
                if (fromUser == true) {
                    mLed.sendWebSocket();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        mGreenText = (TextView) findViewById(R.id.green);
        mGreenLed = (SeekBar) findViewById(R.id.led_green);
        mGreenLed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGreenText.setText("GREEN : " + progress);
                if (mLed == null) {
                    mLed = new LedLight(mSocket);
                }
                mLed.green = progress;
                mLed.sendData();
                if (fromUser == true) {
                    mLed.sendWebSocket();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mBlueText = (TextView) findViewById(R.id.blue);
        mBlueLed = (SeekBar) findViewById(R.id.led_blue);
        mBlueLed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBlueText.setText("BLUE : " + progress);
                if (mLed == null) {
                    mLed = new LedLight(mSocket);
                }
                mLed.blue = progress;
                mLed.sendData();
                if (fromUser == true) {
                    mLed.sendWebSocket();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * lightコマンドを受けた時の処理
     */
    private void executeCommand(Value value) {
        // ADKへ出力
        LedLight light = new LedLight(mSocket);
        light.red = contains(value.getLight().getRed());
        light.green = contains(value.getLight().getGreen());
        light.blue = contains(value.getLight().getBlue());
        light.sendData();
        // 変化を反映する
        mRedLed.setProgress(light.red);
        mGreenLed.setProgress(light.green);
        mBlueLed.setProgress(light.blue);
    }

    private int contains(int value) {
        if (value < 0) {
            return 0;
        } else if (value > 255) {
            return 255;
        }
        return value;
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
                            mSocket = mSocketManager.connect("http://" + editStr);
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
                        mSocket = mSocketManager.connect("http://" + editStr);
                    }
                }).create();
    }
}
