package org.mengdadou.potato.websocket.common;

/**
 * Created by mengdadou on 17-9-25.
 */
public class PrpcConfig {
    public volatile static long READ_TIME_OUT = 50000; //mills
    public volatile static long EXEC_TIME_OUT = 30000; //mills
    public volatile static long CONN_TIME_OUT = 10000; //mills
    
    public final static String BRPC_SEPARATOR = "::";
    public final static String BRPC_HEADER    = "BaaP-RPC-Header";
    public final static String BRPC_CLIENT_IP = "BaaP-RPC-CLIENT-IP";
}
