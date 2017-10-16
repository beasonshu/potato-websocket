package org.mengdadou.potato.websocket.server.util;

import org.mengdadou.potato.websocket.common.exchange.Response;
import org.mengdadou.potato.websocket.common.exchange.ResponseCode;

/**
 * Created by mengdadou on 17-5-16.
 */
public class ResponseUtil {
    public static Response success(long requestId, Object result) {
        return new Response(requestId, result);
    }
    
    public static Response exception(long requestId, Object result) {
        return new Response(requestId, ResponseCode.EXCEPTION.getValue(), result);
    }
}
