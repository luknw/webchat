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
    public void onConnect(Session session) {
        context.startSession(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        context.endSession(session);
    }

    //todo new Message format and a proper class for interpreting
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        context.handleMessage(session, message);
    }
}

