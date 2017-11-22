package org.mengdadou.potato.websocket.common;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SessionWrapper {
    private ReentrantLock lock = new ReentrantLock();
    private Session session;

    private SessionWrapper(Session session) {
        this.session = session;
    }

    public void asyncSendObject(Object o) throws ExecutionException, InterruptedException, RemoteException {
        try {
            if (lock.tryLock(1, TimeUnit.MINUTES)) {
                session.getAsyncRemote().sendObject(o).get();
            } else throw new RemoteException("lock fail!");
        } finally {
            lock.unlock();
        }
    }

    public void close(CloseReason reason) throws IOException {
        session.close(reason);
    }

    public static SessionWrapper of(Session session) {
        return new SessionWrapper(session);
    }
}
