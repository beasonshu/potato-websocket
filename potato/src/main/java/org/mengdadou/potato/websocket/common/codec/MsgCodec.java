package org.mengdadou.potato.websocket.common.codec;


import org.mengdadou.potato.websocket.common.message.PrpcMessage;

/**
 * Created by mengdadou on 16-11-3.
 */
public interface MsgCodec {
    byte[] toBytes(PrpcMessage t);
    
    PrpcMessage toObject(byte[] bytes);
}
