package org.mengdadou.potato.websocket.server.executor;


import org.mengdadou.potato.websocket.common.PrpcConfig;
import org.mengdadou.potato.websocket.common.exchange.Request;
import org.mengdadou.potato.websocket.common.exchange.Response;
import org.mengdadou.potato.websocket.common.exchange.assist.RequestType;
import org.mengdadou.potato.websocket.server.util.ResponseUtil;
import org.mengdadou.potato.websocket.common.message.MessageFactory;
import org.mengdadou.potato.websocket.common.urlresolve.WsURLResolveEnum;
import org.mengdadou.potato.websocket.server.PrpcCtxBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.function.BiFunction;

/**
 * Created by mengdadou on 17-3-24.
 */
public class MethodInvoker {
    private static Logger logger = LoggerFactory.getLogger(MethodInvoker.class);
    
    private Object instance;
    private Method method;
    private int ctxIndex = -1;
    private MethodExecutor executor;
    
    public MethodInvoker(Object instance, Method method, int ctxIndex, MethodExecutor executor) {
        this.instance = instance;
        this.method = method;
        this.ctxIndex = ctxIndex;
        this.executor = executor;
    }
    
    public MethodInvoker(Object instance, Method method, MethodExecutor executor) {
        this.instance = instance;
        this.method = method;
        this.executor = executor;
    }
    
    public void invoke(Request request, Session session) {
        byte type = request.getType();
        if (type == RequestType.NOTIFY.getType()) {
            executor.execute(noResponse.apply(session, request));
        } else {
            // else type is async sync
            try {
                Future<Response> future = executor.submit(needResponse.apply(session, request));
                session.getAsyncRemote()
                        .sendObject(MessageFactory.newMsg(future.get(PrpcConfig.EXEC_TIME_OUT, TimeUnit.MILLISECONDS)))
                        .get();
                logger.debug("request {} done", request.getId());
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.error("future get exception: {}", e.getMessage());
                e.printStackTrace();
                session.getAsyncRemote().sendObject(MessageFactory.newMsg(ResponseUtil.exception(request.getId(), String.format("EXCEPTION : %s", e.getMessage()))));
            }
        }
    }
    
    private BiFunction<Session, Request, Runnable> noResponse = (session, request) ->
            () -> {
                try {
                    if (ctxIndex == 0) {
                        method.invoke(instance, getFinalArgsFromRequest(session, request));
                    } else
                        method.invoke(instance, request.getArgs());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("method invoke exception: {}", e.getMessage());
                    e.printStackTrace();
                }
            };
    
    private BiFunction<Session, Request, Callable<Response>> needResponse = (session, request) ->
            () -> {
                try {
                    Object result;
                    if (ctxIndex == 0) {
                        result = method.invoke(instance, getFinalArgsFromRequest(session, request));
                    } else
                        result = method.invoke(instance, request.getArgs());
                    return ResponseUtil.success(request.getId(), result);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("method invoke exception: {}", e.getMessage());
                    e.printStackTrace();
                    return ResponseUtil.exception(request.getId(), e.getMessage());
                }
            };
    
    private Object[] getFinalArgsFromRequest(Session session, Request request) {
        int length = request.getArgs().length;
        Object[] finalArgs = new Object[length + 1];
        finalArgs[0] = new PrpcCtxBean(session.getRequestURI().getHost(), WsURLResolveEnum.INST.getResolve().getTunnelKey(request.getURL()));
        System.arraycopy(request.getArgs(), 0, finalArgs, 1, length);
        return finalArgs;
    }
    
    public Object getInstance() {
        return instance;
    }
    
    public void setInstance(Object instance) {
        this.instance = instance;
    }
    
    public Method getMethod() {
        return method;
    }
    
    public void setMethod(Method method) {
        this.method = method;
    }
}