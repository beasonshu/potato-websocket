package org.mengdadou.potato.websocket.common.message;

/**
 * Created by mengdadou on 16-12-22.
 */
public enum MessageType {
    SYS((byte) -1),
    BIZ((byte) 1),;
    
    private byte type;
    
    MessageType(byte type) {
        this.type = type;
    }
    
    public byte getType() {
        return type;
    }
}
