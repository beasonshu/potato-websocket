package org.mengdadou.potato.websocket.core.scan;

import org.mengdadou.potato.websocket.core.PrpcCtxBean;
import org.mengdadou.potato.websocket.core.annotation.Prpc;
import org.mengdadou.potato.websocket.core.annotation.PrpcCtx;
import org.mengdadou.potato.websocket.core.annotation.PrpcKey;
import org.mengdadou.potato.websocket.core.executor.MethodExecutor;
import org.mengdadou.potato.websocket.core.executor.MethodInvoker;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by mengdadou on 17-3-23.
 */
public class ScanPrpc {
    
    public ConcurrentHashMap<String, MethodInvoker> findAllPotatoRpc() {
        ConcurrentHashMap<String, MethodInvoker> brpcMap =
                new ConcurrentHashMap<>();
        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchClassesWithAnnotation(Prpc.class, aClass -> this.serviceInit(aClass, brpcMap)).scan(1);
        return brpcMap;
    }
    
    // single thread make safe
    private void serviceInit(Class<?> clazz, ConcurrentHashMap<String, MethodInvoker> brpcMap) {
        Prpc prpc = clazz.getAnnotation(Prpc.class);
        MethodExecutor executor = null;
        Object instance = null;
        
        for (Method a : clazz.getDeclaredMethods()) {
            PrpcKey prpcKey = a.getAnnotation(PrpcKey.class);
            if (prpcKey == null) continue;
            
            String key = getKey(prpcKey);
            
            if (brpcMap.containsKey(key)) {
                MethodInvoker has = brpcMap.get(key);
                throw new RuntimeException(
                        String.format("%s - %s  should not has the same prpc key %s - %s",
                                clazz.getSimpleName(), a.getName(),
                                has.getInstance().getClass().getSimpleName(), has.getMethod().getName())
                );
            } else {
                if (executor == null) executor = new MethodExecutor(prpc.pool());
                try {
                    if (instance == null) instance = clazz.newInstance();
                    
                    boolean hasCtx = false;
                    for (int i = 0; i < a.getParameters().length; i++) {
                        if (a.getParameters()[i].getAnnotation(PrpcCtx.class) == null) {
                            continue;
                        }
                        if (a.getParameters()[i].getType() == PrpcCtxBean.class && i == 0) {
                            hasCtx = true;
                            brpcMap.put(key, new MethodInvoker(instance, a, i, executor));
                        } else {
                            throw new RuntimeException(
                                    String.format("%s parameter must be the first parameter and type must be PrpcCtxBean",
                                            PrpcCtx.class.getSimpleName()));
                        }
                    }
                    if (!hasCtx) brpcMap.put(key, new MethodInvoker(instance, a, executor));
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(
                            String.format("%s must contain a no arg constructor",
                                    clazz.getSimpleName()));
                }
            }
        }
    }
    
    private String getKey(PrpcKey prpcKey) {
        return prpcKey.subtype();
    }
    
    
}
