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
    private static final String NEW_USER = "newUser";
    private static final String TOKEN_SEPARATOR = ":";
    private static final String USER = "User";
    private static final String SERVER = "Server";
    private static final String LEFT_THE_CHAT = " left the chat";
    private static final String JOINED_THE_CHAT = " joined the chat";
    private static final char COMMAND_START = '/';
    private static final String CHANGE_CHANNEL = "changeChannel";

    private final Chat context;

    public ChatWebSocketHandler(Chat context) {
        this.context = context;
    }

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = USER + context.nextUserNumber++;
        context.getUsers().put(user, new User(username, "Coligo", user));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        User userIdentity = context.getUsers().get(user);
        context.getUsers().remove(user);
        if (user != null) {
            context.broadcastMessage(
                    new User(SERVER, userIdentity.getChannelName(), userIdentity.getSession()),
                    userIdentity.getUserName() + LEFT_THE_CHAT);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        if (message.charAt(0) == COMMAND_START) {
            parseMessage(user, message.substring(1));
        } else {
            context.broadcastMessage(context.getUsers().get(user), message);
        }
    }

    private void parseMessage(Session user, String message) {
        String[] tokens = message.split(TOKEN_SEPARATOR);
        switch (tokens[0]) {
            case NEW_USER:
                if (tokens.length < 2) {
                    return;
                } else {
                    newUser(user, tokens[1]);
                }
                break;
            case CHANGE_CHANNEL:
                if (tokens.length < 2) {
                    return;
                }else{
                    changeChannel(user, tokens[1]);
                }
        }
    }

    private void changeChannel(Session user, String target) {

    }

    private void newUser(Session user, String username) {
        User userIdentity = context.getUsers().get(user);
        context.getUsers().remove(user);
        context.getUsers().put(user, new User(username, "Coligo", user));
        context.broadcastMessage(
                new User(SERVER, userIdentity.getChannelName(), userIdentity.getSession()),
                username + JOINED_THE_CHAT);
    }
}
