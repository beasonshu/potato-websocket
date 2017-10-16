package sample;

import org.mengdadou.potato.websocket.server.annotation.PrpcCtx;
import org.mengdadou.potato.websocket.server.PrpcCtxBean;
import org.mengdadou.potato.websocket.server.annotation.Prpc;
import org.mengdadou.potato.websocket.server.annotation.PrpcKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mengdadou on 17-9-25.
 */
@Prpc(pool = 10, desc = "service test for brpc")
public class BrpcServiceDemo {
    private static Logger log = LoggerFactory.getLogger(BrpcServiceDemo.class);
    
    @PrpcKey(subtype = "201")
    public String getName(long id) {
        log.debug("accept param id is {}", id);
        return "this is liming ~~" + id;
    }
    
    @PrpcKey(subtype = "202")
    public String getName(@PrpcCtx PrpcCtxBean bean, long id) {
        return "this is liming ~~" + id;
    }
}
