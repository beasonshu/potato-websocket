package org.mengdadou.potato.websocket.exchange;

/**
 * Created by mengdadou on 17-5-16.
 */
public enum ResponseCode {
    SUCCESS((byte) 1), EXCEPTION((byte) 2),;
    
    private byte value;
    
    ResponseCode(byte value) {
        this.value = value;
    }
    
    public byte getValue() {
        return value;
    }
    
}
