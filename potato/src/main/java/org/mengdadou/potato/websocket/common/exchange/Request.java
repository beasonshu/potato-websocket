package org.mengdadou.potato.websocket.common.exchange;


import org.mengdadou.potato.websocket.common.id.IdGeneratorEnum;

import java.util.Arrays;

/**
 * Created by mengdadou on 17-3-27.
 */
public class Request {
    private byte version;
    
    private long id;
    
    private byte type;
    
    private String URL;
    
    private Object[] args;
    
    public Request() {
    }
    
    public Request(byte type, String URL, Object... args) {
        this.type = type;
        this.URL = URL;
        this.args = args;
        this.version = 0x01;
        this.id = IdGeneratorEnum.INST.getIdGenerator().nextId();
    }
    
    public long getId() {
        return id;
    }
    
    public byte getVersion() {
        return version;
    }
    
    public byte getType() {
        return type;
    }
    
    public String getURL() {
        return URL;
    }
    
    public Object[] getArgs() {
        return args;
    }
    
    @Override
    public String toString() {
        return "Request{" +
                "version=" + version +
                ", id=" + id +
                ", type=" + type +
                ", URL='" + URL + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
