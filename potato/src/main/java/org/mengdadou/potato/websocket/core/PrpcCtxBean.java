package org.mengdadou.potato.websocket.core;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by mengdadou on 17-8-21.
 */
public class PrpcCtxBean {
    private String  remote;
    private Session session;
    
    public PrpcCtxBean(String remote, Session session) {
        this.remote = remote;
        this.session = session;
    }
    
    public String remote() {
        return remote;
    }
    
    public Session session() {
        return session;
    }
    
    public void reset(String reason) throws IOException {
        session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, reason));
    }
    
}
