package org.mengdadou.potato.websocket.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mengdadou on 17-9-25.
 */
public class ConnManager {
    private        ConcurrentHashMap<String, SessionWrapper> connMapping = new ConcurrentHashMap<>();
    private static ConnManager                        connManager = new ConnManager();
    
    private ConnManager() {
    }
    
    public void put(String key, SessionWrapper session) {
        connMapping.put(key, session);
    }
    
    public SessionWrapper remove(String key) {
        return connMapping.remove(key);
    }
    
    public boolean contains(String key) {
        return connMapping.containsKey(key);
    }
    
    public SessionWrapper get(String key) {
        return connMapping.get(key);
    }
    
    public static ConnManager getInstance() {
        return connManager;
    }
}
