package org.mengdadou.potato.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mengdadou on 17-9-25.
 */
public class PrpcConfig {
    public static volatile long DEFAULT_READ_TIME_OUT   = 5 * 60 * 1000; //mills
    public static volatile long DEFAULT_EXEC_TIME_OUT   = DEFAULT_READ_TIME_OUT - 5 * 1000;//mills
    public static volatile long DEFAULT_CONN_TIME_OUT   = 2 * 60 * 1000; //mills
    public static volatile long DEFAULT_HEART_IDLE_TIME = 5 * 60 * 1000; //mills
    
    public final static String BRPC_SEPARATOR  = "::";
    public final static String BRPC_HEADER     = "P-RPC-Header";
    public final static String BRPC_CLIENT_IP  = "P-RPC-CLIENT-IP";
    public final static String WS_URL_PROPERTY = "P-RPC-WS-URL";
    
    public static boolean stati;
    
    static {
        Properties properties = new Properties();
        try {
            InputStream stream = PrpcConfig.class.getResourceAsStream("/prpc.properties");
            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException ignore) {
            // ignore
        }
        String property = properties.getProperty("prpc.stati", "false");
        stati = Boolean.valueOf(property);
    }
    
}
