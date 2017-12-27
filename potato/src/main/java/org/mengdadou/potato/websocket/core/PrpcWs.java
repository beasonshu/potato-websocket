package org.mengdadou.potato.websocket.core;

import org.mengdadou.potato.websocket.PrpcConfig;
import org.mengdadou.potato.websocket.core.util.PrpcUtil;
import org.mengdadou.potato.websocket.message.PrpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.util.concurrent.TimeUnit;

/**
 * Created by mengdadou on 17-9-25.
 */
public class PrpcWs {
    private static Logger         log         = LoggerFactory.getLogger(PrpcWs.class);
    private        PrpcDispatcher dispatcher  = PrpcDispatcher.getInstance();
    private        ConnManager    connManager = ConnManager.getInstance();
    private        long           execTimeout = PrpcConfig.DEFAULT_EXEC_TIME_OUT;
    
    public PrpcWs() {
    }
    
    public PrpcWs setExecTimeout(int t, TimeUnit unit) {
        execTimeout = unit.toMillis(t);
        return this;
    }
    
    public void onOpen(Session session) {
        String tunnelKey = PrpcUtil.getTunnelKey(session);
        log.debug("open session {}/{}", session.getId(), tunnelKey);
        connManager.put(tunnelKey, SessionWrapper.of(session));
    }
    
    public void onMessage(PrpcMessage message, Session session) {
        SessionWrapper sessionWrapper = connManager.get(PrpcUtil.getTunnelKey(session));
        sessionWrapper.updateLastTime();
        ExecThreadLocal.set(execTimeout);
        dispatcher.execute(sessionWrapper, message);
    }
    
    public void onClose(Session session, CloseReason reason) {
        String tunnelKey = PrpcUtil.getTunnelKey(session);
        log.debug("close session {}/{}; reason {}/{}", session.getId(), tunnelKey, reason.getCloseCode(), reason.getReasonPhrase());
        connManager.remove(tunnelKey);
    }
    
    public void onError(Throwable t) {
        t.printStackTrace();
        log.error("error++++ {}", t.getMessage());
    }
}
