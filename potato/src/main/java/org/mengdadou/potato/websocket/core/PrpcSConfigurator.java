package org.mengdadou.potato.websocket.core;

import org.mengdadou.potato.websocket.PrpcConfig;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;


/**
 * Created by mengdadou on 17-9-26.
 */
public class PrpcSConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
        sec.getUserProperties().put(PrpcConfig.BRPC_HEADER, request.getHeaders());
    }
}
