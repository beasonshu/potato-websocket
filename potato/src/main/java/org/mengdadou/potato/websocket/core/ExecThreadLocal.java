package org.mengdadou.potato.websocket.core;

/**
 * Created by mengdadou on 17-12-25.
 */
public class ExecThreadLocal {
    private static ThreadLocal<Object> threadLocal = new ThreadLocal<>();
    
    public static Object get() {
        return threadLocal.get();
    }
    
    public static void set(Object o) {
        threadLocal.set(o);
    }
}
