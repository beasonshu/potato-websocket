### POTATAO-WEBSOCKET 基于websocket传输协议的点对点远程调用框架


POTATAO-WEBSOCKET 是基于websocket传输协议的点对点远程调用框架，框架通过`@Brpc/@BrpcKey`来加载服务，使用`WS URL`的方式来调用服务。  

#### 使用示例
1 建立远程调用服务，如下：
```java
/**
 * Created by mengdadou on 17-9-25.
 */
@Prpc(pool = 10, desc = "service test for brpc")
// rpc服务描述和处理池
public class BrpcServiceDemo {
    private static Logger log = LoggerFactory.getLogger(BrpcServiceDemo.class);
    
    @PrpcKey(subtype = "201")
    // 业务key，全局唯一
    public String getName(long id) {
        log.debug("accept param id is {}", id);
        return "this is liming ~~" + id;
    }
    
    @PrpcKey(subtype = "202")
    // 可以使用@BrpcCtx获取上下文，获取请求的其他属性，目前只包含session/remote
    public String getName(@PrpcCtx PrpcCtxBean bean, long id) {
        return "this is liming ~~" + id;
    }
}
```

2 建立websocket endpoint服务，如下：
```java
/**
 * Created by mengdadou on 17-9-25.
 */
@ServerEndpoint(value = "/sample1/{uuid}/{subtype}", configurator = PrpcServerConfigurator.class, encoders = PrpcEncoder.class, decoders = PrpcDecoder.class)
// 必须指定configurator/encoders/decoders
public class BrpcServerDemo {
    private static PrpcWs prpcWs = new PrpcWs().setExecTimeout(30, TimeUnit.SECONDS);
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("server get client ip*********" + session.getUserProperties().get(PrpcConfig.BRPC_CLIENT_IP));
        prpcWs.onOpen(session);
    }
    
    @OnMessage
    public void onMessage(Session session, PrpcMessage message) {
        prpcWs.onMessage(message, session);
    }
    
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        prpcWs.onClose(session, reason);
    }
    
    @OnError
    public void onError(Throwable t) {
        prpcWs.onError(t);
    }
}
```

3 启动server服务，远程调用
```java
/**
 * Created by mengdadou on 17-10-16.
 */
public class PotatoClientTest {
    public static void main(String[] args) throws RemoteException, ExecutionException, TimeoutException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            Object sync = PrpcHelper.sync("ws://127.0.0.1:8080/sample/sample1/1/201", i);
            System.out.println(sync);
        }
    }
}
```
输出：
```
this is liming ~~0
this is liming ~~1
this is liming ~~2
this is liming ~~3
this is liming ~~4
this is liming ~~5
this is liming ~~6
this is liming ~~7
this is liming ~~8
this is liming ~~9
```

 
4 自定义  

PROTO-WEBSOCKET 支持自定义：   

* 可以自定义编码格式，默认使用的PROTOSTUFF   
* 可以自定义ID格式，默认使用JAVA-AtomLong   
* 可以自定义WsURL的解析方式，默认支持ws://%s:%s/%s/%s/%s/%s的URL规则，其中依次为ip,port,context,key1,uuid,subtype;其中uuid决定channel的唯一性，subtype决定服务的唯一性   
