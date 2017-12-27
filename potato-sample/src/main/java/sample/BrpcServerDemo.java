package sample;

import org.mengdadou.potato.websocket.PrpcConfig;
import org.mengdadou.potato.websocket.codec.PrpcDecoder;
import org.mengdadou.potato.websocket.message.PrpcMessage;
import org.mengdadou.potato.websocket.codec.PrpcEncoder;
import org.mengdadou.potato.websocket.core.PrpcServerConfigurator;
import org.mengdadou.potato.websocket.core.PrpcWs;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.TimeUnit;

/**
 * Created by mengdadou on 17-9-25.
 */
@ServerEndpoint(value = "/sample1/{uuid}/{subtype}", configurator = PrpcServerConfigurator.class, encoders = PrpcEncoder.class, decoders = PrpcDecoder.class)
public class BrpcServerDemo {
    private static PrpcWs prpcWs = new PrpcWs().setExecTimeout(30, TimeUnit.SECONDS);
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("server get client ip*********" + session.getUserProperties().get(PrpcConfig.BRPC_CLIENT_IP));
        prpcWs.onOpen(session);
    }
    
    @OnMessage
    public void onMessage(Session session, PrpcMessage message) {
        prpcWs.onMessage(message, session);
    }
    
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        prpcWs.onClose(session, reason);
    }
    
    @OnError
    public void onError(Throwable t) {
        prpcWs.onError(t);
    }
}
