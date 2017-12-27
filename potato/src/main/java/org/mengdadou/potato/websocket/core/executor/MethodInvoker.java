package org.mengdadou.potato.websocket.core.executor;


import org.mengdadou.potato.websocket.PrpcException;
import org.mengdadou.potato.websocket.core.ExecThreadLocal;
import org.mengdadou.potato.websocket.core.PrpcCtxBean;
import org.mengdadou.potato.websocket.core.SessionWrapper;
import org.mengdadou.potato.websocket.core.stat.PrpcStati;
import org.mengdadou.potato.websocket.core.util.ResponseUtil;
import org.mengdadou.potato.websocket.exchange.Request;
import org.mengdadou.potato.websocket.exchange.Response;
import org.mengdadou.potato.websocket.exchange.assist.RequestType;
import org.mengdadou.potato.websocket.message.MessageFactory;
import org.mengdadou.potato.websocket.urlresolve.WsURLResolveEnum;
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
    
    public void invoke(Request request, SessionWrapper sessionWrapper) {
        byte type = request.getType();
        if (type == RequestType.NOTIFY.getType()) {
            executor.execute(noResponse.apply(sessionWrapper.origin(), request));
        } else {
            // else type is async sync
            Future<Response> future = null;
            try {
                future = executor.submit(needResponse.apply(sessionWrapper.origin(), request));
                
                long execTimeout = (long) ExecThreadLocal.get();
                Response response = future.get(execTimeout, TimeUnit.MILLISECONDS);
                PrpcStati.singelon().sealRespTime(request, response);
                
                sessionWrapper.asyncSendObject(MessageFactory.newMsg(response));
                logger.debug("request {} done", request.getId());
            } catch (InterruptedException | ExecutionException | TimeoutException | PrpcException e) {
                logger.error("future get exception: {}", e.getMessage());
                e.printStackTrace();
                future.cancel(true);
                try {
                    Response response = ResponseUtil.exception(request.getId(), String.format("EXCEPTION : %s", e.getMessage()));
                    PrpcStati.singelon().sealRespTime(request, response);
                    
                    sessionWrapper.asyncSendObject(MessageFactory.newMsg(response));
                } catch (PrpcException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    private BiFunction<Session, Request, Runnable> noResponse = (session, request) ->
            () -> {
                Response response = new Response(request.getId(), null);
                try {
                    if (ctxIndex == 0) {
                        method.invoke(instance, getFinalArgsFromRequest(session, request));
                    } else
                        method.invoke(instance, request.getArgs());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("method invoke exception: {}", e.getMessage());
                    e.printStackTrace();
                }
                PrpcStati.singelon().sealRespTime(request, response);
                PrpcStati.singelon().stati(response);
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
        finalArgs[0] = new PrpcCtxBean(session.getRequestURI().getHost(), session);
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