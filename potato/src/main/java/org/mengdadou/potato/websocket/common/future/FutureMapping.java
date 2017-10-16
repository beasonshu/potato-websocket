package org.mengdadou.potato.websocket.common.future;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mengdadou on 17-8-16.
 */
public class FutureMapping {
    private static FutureMapping mapping = new FutureMapping();
    
    private FutureMapping() {
    }
    
    public static FutureMapping singleton() {
        return mapping;
    }
    
    private ConcurrentHashMap<Long, PrpcFuture> FUTURES
            = new ConcurrentHashMap<>();
    
    public PrpcFuture add(long requestId, PrpcFuture future) {
        return FUTURES.computeIfAbsent(requestId, a -> future);
    }
    
    public PrpcFuture get(long requestId) {
        return FUTURES.get(requestId);
    }
    
    public void remove(long requestId) {
        FUTURES.remove(requestId);
    }
}
