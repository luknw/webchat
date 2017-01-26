package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * PeaceGatch
 * Created by luknw on 17.01.2017
 */

@WebSocket
public class ChatWebSocketHandler {

    private final Chat context;

    public ChatWebSocketHandler(Chat context) {
        this.context = context;
    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        context.addUser(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        context.removeUser(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        context.processMessage(user, message);
    }
}

