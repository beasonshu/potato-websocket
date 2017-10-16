package org.mengdadou.potato.websocket.common.message;


/**
 * Created by mengdadou on 17-1-3.
 */
public class MessageFactory {
    public static PrpcMessage newMsg(Object o) {
        Header header = new Header(MessageType.BIZ.getType());
        return new PrpcMessage(header, o);
    }
}
