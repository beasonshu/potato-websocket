import org.mengdadou.potato.websocket.core.BrpcClient;
import org.mengdadou.potato.websocket.core.PrpcHelper;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by mengdadou on 17-10-16.
 */
public class PotatoClientTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000000; i++) {
            Object sync = BrpcClient.sync("127.0.0.1","8080","sample","sample1","1","201",1L);
            System.out.println(sync);
        }
    }
}
