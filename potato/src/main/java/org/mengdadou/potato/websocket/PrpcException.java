package org.mengdadou.potato.websocket;

/**
 * Created by mengdadou on 17-12-25.
 */
public class PrpcException extends Exception {
    public PrpcException() {
    }
    
    public PrpcException(String message) {
        super(message);
    }
    
    public PrpcException(Throwable cause) {
        super(cause);
    }
    
    public PrpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
