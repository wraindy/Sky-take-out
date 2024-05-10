package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Wraindy
 * @DateTime 2024/05/10 17:23
 * Description
 * Notice
 **/
@Component
@Slf4j
@ServerEndpoint("/ws/{clientId}")
public class WebSocketServer {
    //存放会话对象
    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 连接建立成功的回调函数
     * @param session
     * @param clientId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId) {
        System.out.println("客户端：" + clientId + "建立连接");
        sessionMap.put(clientId, session);
    }

    /**
     * 收到客户端消息的回调方法
     * @param message
     * @param clientId
     */
    @OnMessage
    public void onMessage(String message, @PathParam("clientId") String clientId) {
        System.out.println("收到来自客户端：" + clientId + "的信息:" + message);
    }

    /**
     * 连接关闭时的回调方法
     * @param clientId
     */
    @OnClose
    public void onClose(@PathParam("clientId") String clientId) {
        System.out.println("连接断开:" + clientId);
        sessionMap.remove(clientId);
    }

    /**
     * 群发消息
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
