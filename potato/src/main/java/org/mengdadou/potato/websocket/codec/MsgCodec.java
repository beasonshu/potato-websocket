package org.mengdadou.potato.websocket.codec;


import org.mengdadou.potato.websocket.message.PrpcMessage;

/**
 * Created by mengdadou on 16-11-3.
 */
public interface MsgCodec {
    byte[] toBytes(PrpcMessage t);
    
    PrpcMessage toObject(byte[] bytes);
}
