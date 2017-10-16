package org.mengdadou.potato.websocket.common.listener;

import org.mengdadou.potato.websocket.common.PrpcConfig;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebListener()
public class PrpcServletListener implements ServletRequestListener {
    
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    
    }
    
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        ServletRequest servletRequest = sre.getServletRequest();
        session.setAttribute(PrpcConfig.BRPC_CLIENT_IP, servletRequest.getRemoteHost() + ":" + servletRequest.getRemotePort());
    }
    
}
