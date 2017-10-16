package org.mengdadou.potato.websocket.client;

import org.mengdadou.potato.websocket.common.codec.PrpcDecoder;
import org.mengdadou.potato.websocket.common.codec.PrpcEncoder;
import org.mengdadou.potato.websocket.common.message.PrpcMessage;
import org.mengdadou.potato.websocket.server.PrpcWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.util.concurrent.TimeUnit;

@ClientEndpoint(encoders = PrpcEncoder.class, decoders = PrpcDecoder.class)
public class PrpcClientEndpoint {
    private static Logger log    = LoggerFactory.getLogger(PrpcClientEndpoint.class);
    private static PrpcWs prpcWs = new PrpcWs().setExecTimeout(50, TimeUnit.SECONDS);
    
    
    @OnOpen
    public void onOpen(Session session) {
        log.debug("client open");
        // prpcWs.onOpen(session);
    }
    
    @OnMessage
    public void onMessage(Session session, PrpcMessage message) {
        log.debug("sessionId ======= {}", session.getId());
        prpcWs.onMessage(message, session);
    }
    
    @OnClose
    public void onClose(Session session, CloseReason closereason) {
        log.debug("client close {}", closereason.getReasonPhrase());
        prpcWs.onClose(session, closereason);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.debug("client error");
        throwable.printStackTrace();
        prpcWs.onError(throwable);
    }
    
}
