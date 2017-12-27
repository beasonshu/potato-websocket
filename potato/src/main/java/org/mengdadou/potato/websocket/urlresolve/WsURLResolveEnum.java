package org.mengdadou.potato.websocket.urlresolve;

import org.mengdadou.potato.websocket.util.SpiUtil;

/**
 * Created by mengdadou on 17-10-16.
 */
public enum WsURLResolveEnum {
    INST;
    private WsURLResolve resolve;
    
    WsURLResolveEnum() {
        this.resolve = SpiUtil.getServiceImpl(WsURLResolve.class);
    }
    
    public static WsURLResolve getResolve() {
        return WsURLResolveEnum.INST.resolve;
    }
    
}
