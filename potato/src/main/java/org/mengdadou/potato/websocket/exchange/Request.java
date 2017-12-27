package org.mengdadou.potato.websocket.exchange;


import org.mengdadou.potato.websocket.id.IdGeneratorEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengdadou on 17-3-27.
 */
public class Request {
    private byte version;
    
    private long id;
    
    private byte type;
    
    private String URL;
    
    private Object[] args;
    
    private Map<String, Object> extend;
    
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
    
    public void putExtend(String key, Object value) {
        if (extend == null) {
            synchronized (this) {
                if (extend == null) {
                    extend = new HashMap<>();
                }
            }
        }
        extend.put(key, value);
    }
    
    public void putExtend(Map<String, Object> extend) {
        if (this.extend == null) {
            synchronized (this) {
                if (this.extend == null) {
                    this.extend = new HashMap<>();
                }
            }
        }
        this.extend.putAll(extend);
    }
    
    public Map<String, Object> getExtend() {
        return extend;
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
