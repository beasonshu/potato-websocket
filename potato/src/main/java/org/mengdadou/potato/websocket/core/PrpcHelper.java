package org.mengdadou.potato.websocket.core;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.mengdadou.potato.websocket.PrpcConfig;
import org.mengdadou.potato.websocket.PrpcException;
import org.mengdadou.potato.websocket.core.stat.PrpcStati;
import org.mengdadou.potato.websocket.exchange.Request;
import org.mengdadou.potato.websocket.exchange.Response;
import org.mengdadou.potato.websocket.exchange.assist.RequestType;
import org.mengdadou.potato.websocket.future.FutureMapping;
import org.mengdadou.potato.websocket.future.PrpcFuture;
import org.mengdadou.potato.websocket.message.MessageFactory;
import org.mengdadou.potato.websocket.urlresolve.WsURLResolve;
import org.mengdadou.potato.websocket.urlresolve.WsURLResolveEnum;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mengdadou on 17-9-25.
 */
public class PrpcHelper {
    private static ConnManager connManager = ConnManager.getInstance();
    
    private static final ClientManager clientManager = ClientManager.createClient();
    
    
    static {
        clientManager.getProperties().put(ClientProperties.HANDSHAKE_TIMEOUT, 1000 * 60);
    }
    
    private static Class endpoint = PrpcClientEndpoint.class;
    
    protected static void reset(String wsURL, String reason) throws IOException {
        String tunnelKey = WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL);
        SessionWrapper sessionWrapper = connManager.remove(tunnelKey);
        if (sessionWrapper != null)
            sessionWrapper.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, reason));
    }
    
    protected static Object sync(String wsURL, boolean overSSL, Object... data)
            throws PrpcException, InterruptedException, ExecutionException, TimeoutException {
        return async(wsURL, overSSL, data).get(PrpcConfig.DEFAULT_READ_TIME_OUT, TimeUnit.MILLISECONDS);
    }
    
    protected static PrpcFuture async(String wsURL, boolean overSSL, Object... data)
            throws PrpcException {
        Request request = new Request(RequestType.SYNC.getType(), wsURL, data);
        PrpcFuture future = new PrpcFuture(request.getId());
        
        PrpcStati.singelon().sealReqTime(request);
        
        sendObject(wsURL, MessageFactory.newMsg(request), overSSL);
        return future;
    }
    
    protected static void notify(String wsURL, boolean overSSL, Object... data)
            throws PrpcException {
        Request request = new Request(RequestType.NOTIFY.getType(), wsURL, data);
        
        PrpcStati.singelon().sealReqTime(request);
        
        sendObject(wsURL, MessageFactory.newMsg(request), overSSL);
    }
    
    private static void sendObject(String wsURL, Object object, boolean overSSL) throws PrpcException {
        SessionWrapper sessionWrapper = connect(wsURL);
        if (sessionWrapper != null) {
            sessionWrapper.asyncSendObject(object);
        }
    }
    
    private static SessionWrapper connect(String wsURL) {
        if (connManager.contains(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL)))
            return connManager.get(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL));
        return syncConnect(wsURL);
    }
    
    private synchronized static SessionWrapper syncConnect(String wsURL) {
        if (connManager.contains(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL)))
            return connManager.get(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL));
        return connR(wsURL);
    }
    
    // not thread safe
    // means that will create N conn to one remote
    // remote can be any content
    // but you should impl the URLHandler for get the host
    private static SessionWrapper connR(String wsURL) {
        try {
            Session session = clientManager.connectToServer(endpoint, URI.create(wsURL));
            SessionWrapper wrapper = SessionWrapper.of(session);
            connManager.put(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL), wrapper);
            session.getUserProperties().put("wsURL", wsURL);
            return wrapper;
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    protected static void writeResp(Response response) {
        PrpcFuture f = FutureMapping.singleton().get(response.getId());
        if (f == null) return;
        f.set(response);
    }
    
    protected static void shutdownClient() {
        clientManager.shutdown();
    }
}
