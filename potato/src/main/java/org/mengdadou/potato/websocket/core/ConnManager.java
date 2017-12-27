package org.mengdadou.potato.websocket.core;

import org.mengdadou.potato.websocket.PrpcConfig;
import org.mengdadou.potato.websocket.PrpcException;
import org.mengdadou.potato.websocket.message.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by mengdadou on 17-9-25.
 */
public class ConnManager {
    private static  Logger                                    log         = LoggerFactory.getLogger(ConnManager.class);
    private        ConcurrentHashMap<String, SessionWrapper> connMapping = new ConcurrentHashMap<>();
    private static ConnManager                               connManager = new ConnManager();
    
    private ConnManager() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Collection<SessionWrapper> sessionWrappers = connMapping.values();
            for (SessionWrapper session : sessionWrappers) {
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Jvm Down!"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PrpcHelper.shutdownClient();
            log.info("shutdown websocket ..........");
        }));
        new HeartBeatHandler(this).start();
    }
    
    public void put(String key, SessionWrapper sessionWrapper) {
        log.debug("key {} -> collect {}", key, connMapping.keySet());
        connMapping.put(key, sessionWrapper);
    }
    
    public SessionWrapper remove(String key) {
        log.debug("key {} -> collect {}", key, connMapping.keySet());
        return connMapping.remove(key);
    }
    
    public boolean contains(String key) {
        return connMapping.containsKey(key);
    }
    
    public SessionWrapper get(String key) {
        return connMapping.get(key);
    }
    
    protected static ConnManager getInstance() {
        return connManager;
    }
    
    private static class HeartBeatHandler {
        private ConnManager connManager;
        
        HeartBeatHandler(ConnManager connManager) {
            this.connManager = connManager;
        }
        
        void start() {
            Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
                    r, 1000 * 60 * 2, 1000 * 60 * 2, TimeUnit.MILLISECONDS);
        }
        
        private Runnable r = () -> connManager.connMapping.values().forEach(a -> {
            if (System.currentTimeMillis() - a.lastTime() > PrpcConfig.DEFAULT_HEART_IDLE_TIME) {
                try {
                    a.asyncSendObject(MessageFactory.newBeat());
                } catch (PrpcException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
