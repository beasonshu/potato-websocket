package org.mengdadou.potato.websocket.common.codec;

import org.mengdadou.potato.websocket.common.util.SpiUtil;
import org.mengdadou.potato.websocket.common.message.PrpcMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.nio.ByteBuffer;

/**
 * Created by mengdadou on 17-9-26.
 */
public class PrpcDecoder implements Decoder.Binary<PrpcMessage> {
    private static ClassLoader loader = PrpcDecoder.class.getClassLoader();
    private static MsgCodec msgCodec = SpiUtil.getServiceImpl(MsgCodec.class);
    
    @Override
    public PrpcMessage decode(ByteBuffer byteBuffer) throws DecodeException {
        Thread.currentThread().setContextClassLoader(loader);
        return msgCodec.toObject(byteBuffer.array());
    }
    
    @Override
    public boolean willDecode(ByteBuffer byteBuffer) {
        return true;
    }
    
    @Override
    public void init(EndpointConfig endpointConfig) {
    
    }
    
    @Override
    public void destroy() {
    
    }
}
