package org.mengdadou.potato.websocket.codec;

import org.mengdadou.potato.websocket.message.PrpcMessage;
import org.mengdadou.potato.websocket.util.SpiUtil;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.nio.ByteBuffer;

/**
 * Created by mengdadou on 17-9-26.
 */
public class PrpcEncoder implements Encoder.Binary<PrpcMessage> {
    private static ClassLoader loader = PrpcDecoder.class.getClassLoader();
    private static MsgCodec msgCodec = SpiUtil.getServiceImpl(MsgCodec.class);
    
    @Override
    public ByteBuffer encode(PrpcMessage message) throws EncodeException {
        Thread.currentThread().setContextClassLoader(loader);
        return ByteBuffer.wrap(msgCodec.toBytes(message));
    }
    
    @Override
    public void init(EndpointConfig endpointConfig) {
    
    }
    
    @Override
    public void destroy() {
    
    }
}
