package org.mengdadou.potato.websocket.core;


import org.mengdadou.potato.websocket.PrpcException;
import org.mengdadou.potato.websocket.core.executor.MethodInvoker;
import org.mengdadou.potato.websocket.core.scan.ScanPrpc;
import org.mengdadou.potato.websocket.core.stat.PrpcStati;
import org.mengdadou.potato.websocket.core.util.PrpcUtil;
import org.mengdadou.potato.websocket.core.util.ResponseUtil;
import org.mengdadou.potato.websocket.exchange.Request;
import org.mengdadou.potato.websocket.exchange.Response;
import org.mengdadou.potato.websocket.message.MessageFactory;
import org.mengdadou.potato.websocket.message.MessageType;
import org.mengdadou.potato.websocket.message.PrpcMessage;
import org.mengdadou.potato.websocket.urlresolve.WsURLResolveEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by mengdadou on 17-3-27.
 */
public class PrpcDispatcher {
    private static Logger log = LoggerFactory.getLogger(PrpcDispatcher.class);
    private Map<String, MethodInvoker> brpcMap;
    private static PrpcDispatcher dispatcher = new PrpcDispatcher();
    
    private PrpcDispatcher() {
        this.brpcMap = new ScanPrpc().findAllPotatoRpc();
    }
    
    static PrpcDispatcher getInstance() {
        return dispatcher;
    }
    
    void execute(SessionWrapper sessionWrapper, PrpcMessage message) {
        if (message.getHeader().getType() == MessageType.BEAT.getType()) {
            return;
        }
        
        Object o = message.getBody();
        
        if (o instanceof Response) {
            PrpcStati.singelon().sealRespArrivedTime((Response) o);
            PrpcStati.singelon().stati((Response) o);
            
            PrpcHelper.writeResp((Response) o);
            return;
        }
        
        Request request = (Request) o;
        PrpcStati.singelon().sealReqArrivedTime(request);
        log.debug("request {} start from ip:{}, request URL : {}", request.getId(), PrpcUtil.getHost(sessionWrapper.origin()), request.getURL());
        
        MethodInvoker invoker = brpcMap.get(WsURLResolveEnum.getResolve().getSubtype(request.getURL()));
        if (invoker == null) {
            try {
                Response response = ResponseUtil.exception(request.getId(), "no service find for this path");
                PrpcStati.singelon().sealRespTime(request, response);
                
                sessionWrapper.asyncSendObject(MessageFactory.newMsg(response));
            } catch (PrpcException e) {
                e.printStackTrace();
            }
            return;
        }
        
        invoker.invoke(request, sessionWrapper);
    }
}
