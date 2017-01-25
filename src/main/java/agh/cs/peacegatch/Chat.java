package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Spark;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class Chat {
    int nextUserNumber = 1;
    private Map<String, List<User>> channels = new ConcurrentHashMap<>();
    private Map<Session, User> userIdentityMap = new ConcurrentHashMap<>();

    public Map<Session, User> getUsers() {
        return userIdentityMap;
    }

    void init() {
        Spark.staticFileLocation("/public");
        Spark.webSocket("/chat", new ChatWebSocketHandler(this));
        Spark.init();
    }

    public void broadcastMessage(User sender, String message) {
        List<User> userList = userIdentityMap.entrySet().stream()
                .filter(e -> e.getKey().isOpen())
                .filter(e -> e.getValue().getChannelName()
                        .equals(sender.getChannelName()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<String> userNames = userList.stream().map(User::getUserName).collect(Collectors.toList());

        userList.forEach(user -> {
            try {
                user.getSession().getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(
                                sender.getUserName(), message))
                        .put("userList", userNames)
                        .put("channelList", channels.keySet())
                        .put("currentChannel", sender.getChannelName())
                ));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }

}
