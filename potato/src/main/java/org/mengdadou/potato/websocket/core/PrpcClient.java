package org.mengdadou.potato.websocket.core;

import org.mengdadou.potato.websocket.future.PrpcFuture;

import java.io.IOException;

public class PrpcClient {
    
    private static String template        = "ws://%s:%s/%s/%s/%s/%s";
    
    public static Object sync(String ip, String port, String context, String from, String uuid,
                              String subtype, Object... param) throws Exception {
        return PrpcHelper.sync(String.format(template, ip, port, context, from, uuid, subtype),
                false, param);
    }
    
    public static PrpcFuture async(String ip, String port, String context, String from,
                                   String uuid, String subtype, Object... param) throws Exception {
        return PrpcHelper.async(String.format(template, ip, port, context, from, uuid, subtype),
                false, param);
    }
    
    public static void notify(String ip, String port, String context, String from, String uuid,
                              String subtype, Object... param) throws Exception {
        PrpcHelper.notify(String.format(template, ip, port, context, from, uuid, subtype), false,
                param);
    }
    
    public static void reset(String ip, String port, String context, String from, String uuid, String reason) {
        try {
            PrpcHelper.reset(String.format(template, ip, port, context, from, uuid, "ignore"), reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
