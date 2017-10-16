package org.mengdadou.potato.websocket.common.message;

/**
 * Created by mengdadou on 17-9-26.
 */
public class PrpcMessage {
    private Header header;
    private Object body;
    
    public PrpcMessage() {
    }
    
    public PrpcMessage(Header header) {
        this.header = header;
    }
    
    public PrpcMessage(Header header, Object body) {
        this.header = header;
        this.body = body;
    }
    
    public Header getHeader() {
        return header;
    }
    
    public void setHeader(Header header) {
        this.header = header;
    }
    
    public Object getBody() {
        return body;
    }
    
    public void setBody(Object body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "PrpcMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
