package org.mengdadou.potato.websocket.message;


/**
 * Created by mengdadou on 17-1-3.
 */
public class MessageFactory {
    public static PrpcMessage newMsg(Object o) {
        Header header = new Header(MessageType.BIZ.getType());
        return new PrpcMessage(header, o);
    }
    
    public static PrpcMessage newBeat() {
        Header header = new Header(MessageType.BEAT.getType());
        return new PrpcMessage(header);
    }
}
