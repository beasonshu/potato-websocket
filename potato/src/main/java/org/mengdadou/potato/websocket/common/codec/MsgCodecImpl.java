package org.mengdadou.potato.websocket.common.codec;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.mengdadou.potato.websocket.common.message.PrpcMessage;

/**
 * Created by mengdadou on 17-10-16.
 */
public class MsgCodecImpl implements MsgCodec {
    
    private Schema<PrpcMessage> schema = RuntimeSchema.createFrom(PrpcMessage.class);
    
    public byte[] toBytes(PrpcMessage t) {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(t, schema, buffer);
    }
    
    public PrpcMessage toObject(byte[] bytes) {
        try {
            PrpcMessage msg = new PrpcMessage();
            ProtostuffIOUtil.mergeFrom(bytes, msg, schema);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
