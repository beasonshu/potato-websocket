package org.mengdadou.potato.websocket.exchange.assist;

/**
 * Created by mengdadou on 17-3-27.
 */
public enum RequestType {
    SYNC((byte) 1),     // 同步请求,会收到回复
    NOTIFY((byte) 2);   // 异步通知,相当于UDP

    private byte type;

    RequestType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }
}
