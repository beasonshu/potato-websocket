package org.mengdadou.potato.websocket.common;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mengdadou on 17-9-25.
 */
public class ConnManager {
    private        ConcurrentHashMap<String, Session> connMapping = new ConcurrentHashMap<>();
    private static ConnManager                        connManager = new ConnManager();
    
    private ConnManager() {
    }
    
    public void put(String key, Session session) {
        connMapping.put(key, session);
    }
    
    public Session remove(String key) {
        return connMapping.remove(key);
    }
    
    public boolean contains(String key) {
        return connMapping.containsKey(key);
    }
    
    public Session get(String key) {
        return connMapping.get(key);
    }
    
    public static ConnManager getInstance() {
        return connManager;
    }
}
