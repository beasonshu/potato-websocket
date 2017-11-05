package org.mengdadou.potato.websocket.common;

import org.mengdadou.potato.websocket.common.exchange.Request;
import org.mengdadou.potato.websocket.common.exchange.Response;
import org.mengdadou.potato.websocket.common.exchange.assist.RequestType;
import org.mengdadou.potato.websocket.common.future.FutureMapping;
import org.mengdadou.potato.websocket.client.PrpcClientEndpoint;
import org.mengdadou.potato.websocket.common.future.PrpcFuture;
import org.mengdadou.potato.websocket.common.message.MessageFactory;
import org.mengdadou.potato.websocket.common.urlresolve.WsURLResolveEnum;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mengdadou on 17-9-25.
 */
public class PrpcHelper {
    private static ConnManager connManager = ConnManager.getInstance();

    private static final WebSocketContainer container = ContainerProvider.getWebSocketContainer();

    private static Class endpoint = PrpcClientEndpoint.class;

    public static void reset(String brpcKey, String reason) throws IOException {
        connManager.remove(brpcKey).close(
                new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, reason));
    }

    public static Object sync(String wsURL, Object... data) throws RemoteException,
            InterruptedException, ExecutionException, TimeoutException {
        return async(wsURL, data).get(PrpcConfig.READ_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public static PrpcFuture async(String wsURL, Object... data) throws RemoteException,
            ExecutionException, InterruptedException {
        Request request = new Request(RequestType.SYNC.getType(), wsURL, data);
        PrpcFuture future = new PrpcFuture(request.getId());
        connect(wsURL).getAsyncRemote().sendObject(MessageFactory.newMsg(request)).get();
        return future;
    }

    public static void notify(String wsURL, Object... data) throws RemoteException,
            InterruptedException, ExecutionException {
        Request request = new Request(RequestType.NOTIFY.getType(), wsURL, data);
        connect(wsURL).getAsyncRemote().sendObject(MessageFactory.newMsg(request)).get();
    }

    private static Session connect(String wsURL) {
        if (connManager.contains(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL)))
            return connManager.get(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL));
        return syncConnect(wsURL);
    }

    private synchronized static Session syncConnect(String wsURL) {
        if (connManager.contains(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL)))
            return connManager.get(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL));
        return connR(wsURL);
    }

    // not thread safe
    // means that will create N conn to one remote
    // remote can be any content
    // but you should impl the URLHandler for get the host
    private static Session connR(String wsURL) {
        try {
            Session session = container.connectToServer(endpoint, URI.create(wsURL));
            connManager.put(WsURLResolveEnum.INST.getResolve().getTunnelKey(wsURL), session);
            session.getUserProperties().put("wsURL", wsURL);
            return session;
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeResp(Response response) {
        PrpcFuture f = FutureMapping.singleton().get(response.getId());
        if (f == null) return;
        f.set(response);
    }
}
