package org.mengdadou.potato.websocket.core;

import org.mengdadou.potato.websocket.PrpcConfig;
import org.mengdadou.potato.websocket.message.PrpcMessage;
import org.mengdadou.potato.websocket.urlresolve.WsURLResolveEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by mengdadou on 17-9-25.
 */
public class PrpcWs {
    private static Logger         log         = LoggerFactory.getLogger(PrpcWs.class);
    private        PrpcDispatcher dispatcher  = new PrpcDispatcher();
    private        ConnManager    connManager = ConnManager.getInstance();
    
    public PrpcWs() {
    }
    
    public PrpcWs setExecTimeout(int t, TimeUnit unit) {
        PrpcConfig.EXEC_TIME_OUT = unit.toMillis(t);
        return this;
    }
    
    public void onOpen(Session session) {
        String tunnelKey = handleSession.apply(session);
        log.debug("open session {}/{}", session.getId(), tunnelKey);
        connManager.put(tunnelKey, SessionWrapper.of(session));
    }
    
    public void onMessage(PrpcMessage message, Session session) {
        dispatcher.execute(session, message);
    }
    
    public void onClose(Session session, CloseReason reason) {
        String tunnelKey = handleSession.apply(session);
        log.debug("close session {}/{}; reason {}/{}", session.getId(), tunnelKey, reason.getCloseCode(), reason.getReasonPhrase());
        connManager.remove(tunnelKey);
    }
    
    private Function<Session, String> handleSession = session -> {
//        if (session.getRequestURI() != null && session.getRequestURI().toString().startsWith("ws://")) {
        Object object = session.getUserProperties().get("wsURL");
        if (object != null) {
            return WsURLResolveEnum.INST.getResolve().getTunnelKey((String) object);
        } else {
            //noinspection unchecked
            String addr = (String) session.getUserProperties().get(PrpcConfig.BRPC_CLIENT_IP);
            String tunnelKey = WsURLResolveEnum.INST.getResolve().getTunnelKey("ws://" + addr + session.getRequestURI().toString());
            log.debug("server tunnelKey+++" + tunnelKey);
            return tunnelKey;
        }
    };
    
    public void onError(Throwable t) {
        t.printStackTrace();
        log.error("error++++" + t.getMessage());
    }
}
