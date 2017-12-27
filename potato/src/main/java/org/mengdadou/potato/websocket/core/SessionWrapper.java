package org.mengdadou.potato.websocket.core;

import org.mengdadou.potato.websocket.PrpcException;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SessionWrapper {
    private ReentrantLock lock = new ReentrantLock();
    private Session session;
    private long    lastTime;
    
    private SessionWrapper(Session session) {
        this.session = session;
        this.lastTime = System.currentTimeMillis();
    }
    
    public static SessionWrapper of(Session session) {
        return new SessionWrapper(session);
    }
    
    public void asyncSendObject(Object o) throws PrpcException {
        try {
            if (lock.tryLock(1, TimeUnit.MINUTES)) {
                session.getAsyncRemote().sendObject(o).get();
            } else throw new PrpcException("lock fail!");
        } catch (InterruptedException | ExecutionException e) {
            throw new PrpcException(e);
        } finally {
            lock.unlock();
        }
    }
    
    public void close(CloseReason reason) throws IOException {
        session.close(reason);
    }
    
    public long lastTime() {
        return lastTime;
    }
    
    public Session origin() {
        return session;
    }
    
    public void updateLastTime() {
        lastTime = System.currentTimeMillis();
    }
}
