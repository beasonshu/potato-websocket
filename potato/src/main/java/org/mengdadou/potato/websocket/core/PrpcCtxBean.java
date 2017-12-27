package org.mengdadou.potato.websocket.core;

import java.io.IOException;

/**
 * Created by mengdadou on 17-8-21.
 */
public class PrpcCtxBean {
    private String remote;
    private String tunnelKey;
    
    
    public PrpcCtxBean(String remote, String tunnelKey) {
        this.remote = remote;
        this.tunnelKey = tunnelKey;
    }
    
    public void reset(String reason) throws IOException {
        PrpcHelper.reset(tunnelKey, reason);
    }
    
    public String remote() {
        return remote;
    }
    
}
