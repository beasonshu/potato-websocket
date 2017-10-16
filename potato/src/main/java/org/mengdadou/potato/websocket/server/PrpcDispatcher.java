package org.mengdadou.potato.websocket.server;


import org.mengdadou.potato.websocket.common.exchange.Request;
import org.mengdadou.potato.websocket.server.scan.ScanPrpc;
import org.mengdadou.potato.websocket.server.util.ResponseUtil;
import org.mengdadou.potato.websocket.common.PrpcHelper;
import org.mengdadou.potato.websocket.common.exchange.Response;
import org.mengdadou.potato.websocket.common.message.PrpcMessage;
import org.mengdadou.potato.websocket.common.message.MessageFactory;
import org.mengdadou.potato.websocket.common.urlresolve.WsURLResolveEnum;
import org.mengdadou.potato.websocket.server.executor.MethodInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mengdadou on 17-3-27.
 */
public class PrpcDispatcher {
    private static Logger log = LoggerFactory.getLogger(PrpcDispatcher.class);
    private ConcurrentHashMap<String, MethodInvoker> brpcMap;
    
    public PrpcDispatcher() {
        this.brpcMap = new ScanPrpc().findAllPotatoRpc();
    }
    
    public void execute(Session session, PrpcMessage message) {
        Object o = message.getBody();
        if (o instanceof Response) {
            PrpcHelper.writeResp((Response) o);
            return;
        }
        
        Request request = (Request) o;
        log.debug("request {} start", request.getId());
        MethodInvoker invoker = brpcMap.get(WsURLResolveEnum.INST.getResolve().getSubtype(request.getURL()));
        if (invoker == null) {
            session.getAsyncRemote().sendObject(MessageFactory.newMsg(ResponseUtil.exception(request.getId(), "no service find for this path")));
            return;
        }
        
        invoker.invoke(request, session);
    }
}
