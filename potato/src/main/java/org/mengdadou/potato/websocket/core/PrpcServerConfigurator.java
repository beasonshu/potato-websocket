package org.mengdadou.potato.websocket.core;

import org.mengdadou.potato.websocket.PrpcConfig;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class PrpcServerConfigurator extends Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request,
            HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(PrpcConfig.BRPC_CLIENT_IP, httpSession.getAttribute(PrpcConfig.BRPC_CLIENT_IP));
    }
    
}
