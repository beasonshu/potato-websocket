package org.mengdadou.potato.websocket.common.urlresolve;

import org.mengdadou.potato.websocket.common.PrpcConfig;
import org.apache.commons.collections4.map.LRUMap;

import java.util.function.Function;

/**
 * Created by mengdadou on 17-3-27.
 */
public class WsURLResolveImpl implements WsURLResolve {
    private LRUMap<String, String> subtypeMapping   = new LRUMap<>(5000);
    private LRUMap<String, String> tunnelKeyMapping = new LRUMap<>(5000);
    
    public String getSubtype(String wsURL) {
        return subtypeMapping.computeIfAbsent(wsURL, a -> subtypeFunc.apply(a));
    }
    
    private Function<String, String> subtypeFunc = wsURL ->
            wsURL.substring(wsURL.lastIndexOf("/") + 1);
    
    public String getTunnelKey(String wsURL) {
        return tunnelKeyMapping.computeIfAbsent(wsURL, a -> tunnelKeyFunc.apply(a));
    }
    
    private Function<String, String> tunnelKeyFunc = wsURL -> {
        if (wsURL.startsWith("wss://")) wsURL = wsURL.replace("wss://", "");
        if (wsURL.startsWith("ws://")) wsURL = wsURL.replace("ws://", "");
        return wsURL.substring(0, wsURL.indexOf("/")).split(":")[0] + PrpcConfig.BRPC_SEPARATOR
                + wsURL.substring(getStringIndex(wsURL, "/", 3) + 1, getStringIndex(wsURL, "/", 4));
    };
    
    private int getStringIndex(String origin, @SuppressWarnings("SameParameterValue") String str, int n) {
        int index = 0;
        int current = 0;
        for (int i = 0; i < n; i++) {
            index = origin.indexOf(str, current);
            if (index == -1) return -1;
            current = str.length() + index;
        }
        return index;
    }
    
}
