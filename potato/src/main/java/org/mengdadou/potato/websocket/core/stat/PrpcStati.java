package org.mengdadou.potato.websocket.core.stat;

import org.mengdadou.potato.websocket.PrpcConfig;
import org.mengdadou.potato.websocket.exchange.Request;
import org.mengdadou.potato.websocket.exchange.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mengdadou on 17-12-25.
 */
@SuppressWarnings("FieldCanBeLocal")
public class PrpcStati {
    private static Logger logger = LoggerFactory.getLogger(PrpcStati.class);
    
    private String reqTime         = "BRPC_REQ_TIME";
    private String reqArrivedTime  = "BRPC_ACCEPT_REQ_TIME";
    private String respTime        = "BRPC_RESP_TIME";
    private String respArrivedTime = "BRPC_ACCEPT_RESP_TIME";
    
    private static PrpcStati instance = new PrpcStati();
    
    public static PrpcStati singelon() {
        return instance;
    }
    
    
    public void sealReqTime(Request req) {
        if (PrpcConfig.stati)
            req.putExtend(reqTime, System.currentTimeMillis());
    }
    
    public void sealReqArrivedTime(Request req) {
        if (PrpcConfig.stati)
            req.putExtend(reqArrivedTime, System.currentTimeMillis());
    }
    
    public void sealRespTime(Request req, Response resp) {
        if (PrpcConfig.stati) {
            req.getExtend().forEach(resp::putExtend);
            sealRespTime(resp);
        }
    }
    
    public void sealRespTime(Response resp) {
        if (PrpcConfig.stati)
            resp.putExtend(respTime, System.currentTimeMillis());
    }
    
    public void sealRespArrivedTime(Response resp) {
        if (PrpcConfig.stati)
            resp.putExtend(respArrivedTime, System.currentTimeMillis());
    }
    
    public void stati(Response response) {
        if (PrpcConfig.stati) {
            long req = (long) get(response, reqTime);
            long reqArrived = (long) get(response, reqArrivedTime);
            long resp = (long) get(response, respTime);
            long respArrived = (long) get(response, respArrivedTime);
            
            if (req != 0 && reqArrived != 0)
                logger.info("{} req  transfer take {} ms", response.getId(), reqArrived - req);
            
            if (reqArrived != 0 && resp != 0)
                logger.info("{} biz  exec     take {} ms", response.getId(), resp - reqArrived);
            
            if (resp != 0 && respArrived != 0)
                logger.info("{} resp transfer take {} ms", response.getId(), respArrived - resp);
            
            if (respArrived != 0 && req != 0)
                logger.info("{} call whole    take {} ms", response.getId(), reqArrived - req);
        }
    }
    
    private Object get(Response resp, String key) {
        return resp.getExtend().getOrDefault(key, 0L);
    }
}
