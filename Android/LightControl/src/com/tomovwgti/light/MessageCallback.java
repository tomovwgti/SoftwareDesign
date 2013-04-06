/**
 * 
 */

package com.tomovwgti.light;

import com.tomovwgti.json.Msg;

/**
 * @author tomo
 */
public interface MessageCallback {
    /** 切断されたとき */
    public void onDisconnect();

    /** 接続したとき */
    public void onConnect();

    /** HERTBEATを受信したとき */
    public void onHertbeat();

    /** メッセージを受信したとき */
    public void onMessage();

    /** JSONメッセージを受信したとき */
    public void onJsonMessage(Msg message);

    /** イベントを受信したとき */
    public void onEvent();

    /** エラーを受信したとき */
    public void onError();

    /** Ackメッセージを受信したとき */
    public void onAck();
}
