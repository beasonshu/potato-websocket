package org.mengdadou.potato.websocket.core.util;

import org.mengdadou.potato.websocket.PrpcConfig;
import org.mengdadou.potato.websocket.urlresolve.WsURLResolveEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;

/**
 * Created by mengdadou on 17-11-3.
 */
public class PrpcUtil {
    private static Logger log = LoggerFactory.getLogger(PrpcUtil.class);
    
    public static String getTunnelKey(Session session) {
        Object object = session.getUserProperties().get(PrpcConfig.WS_URL_PROPERTY);
        if (object != null) {
            return WsURLResolveEnum.getResolve().getTunnelKey((String) object);
        } else {
            //noinspection unchecked
            String addr = (String) session.getUserProperties().get(PrpcConfig.BRPC_CLIENT_IP);
            String tunnelKey = WsURLResolveEnum.getResolve().getTunnelKey("ws://" + addr + session.getRequestURI().toString());
            log.debug("server tunnelKey {}", tunnelKey);
            return tunnelKey;
        }
    }
    
    
    public static String getHost(Session session) {
        return getTunnelKey(session).split(PrpcConfig.BRPC_SEPARATOR)[0];
    }
}
