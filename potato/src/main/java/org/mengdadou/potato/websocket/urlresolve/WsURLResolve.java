package org.mengdadou.potato.websocket.urlresolve;

/**
 * Created by mengdadou on 17-10-16.
 */
public interface WsURLResolve {
    String getSubtype(String wsURL);
    
    String getTunnelKey(String wsURL);
}
