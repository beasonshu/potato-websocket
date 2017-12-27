package org.mengdadou.potato.websocket.exchange;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengdadou on 17-3-27.
 */
public class Response {
    private long id;
    private byte code = 1;
    private Object data;
    
    private Map<String, Object> extend;
    
    public Response() {
    }
    
    public Response(long id, Object data) {
        this.id = id;
        this.data = data;
    }
    
    public Response(long id, byte code, Object data) {
        this.id = id;
        this.code = code;
        this.data = data;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public byte getCode() {
        return code;
    }
    
    public void setCode(byte code) {
        this.code = code;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
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
        return "Response{" +
                "id=" + id +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
