package org.mengdadou.potato.websocket.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mengdadou on 17-9-27.
 */
public class IdGeneratorImpl implements IdGenerator {
    private static AtomicLong idWorker = new AtomicLong();
    
    public long nextId() {
        return idWorker.getAndIncrement();
    }
}
