package org.mengdadou.potato.websocket.message;

/**
 * Created by mengdadou on 16-11-4.
 */
public class Header {
    private byte version;
    // see {@link MessageType}
    private byte type;
    
    public Header() {
        this.version = 0x01;
    }
    
    public Header(byte type) {
        this.type = type;
    }
    
    public byte getVersion() {
        return version;
    }
    
    public byte getType() {
        return type;
    }
    
    public void setType(byte type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "Header{" +
                "version=" + version +
                ", type=" + type +
                '}';
    }
}
