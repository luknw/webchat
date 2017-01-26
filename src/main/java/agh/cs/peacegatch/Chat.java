package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Spark;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class Chat {
    static final String DEFAULT_CHANNEL = "Chatbot";
    static final String SERVER = "Server";
    private static final String NEW_USER = "newUser";
    private static final String USER = "User";
    private static final String TOKEN_SEPARATOR = ":";
    private static final String STATIC_FILE_LOCATION = "/public";
    private static final String WEBSOCKET_PATH = "/chat";
    private static final String JOINED_THE_CHAT = " joined the chat";
    private static final String LEFT_THE_CHAT = " left the chat";
    private static final String SWITCH_CHANNEL = "switchChannel";
    private static final String ADD_CHANNEL = "addChannel";
    private static final String TIMESTAMP = "timestamp";
    private static final String SAYS = " says:";
    private static final String HH_MM_SS = "HH:mm:ss";
    private static final char COMMAND_START = '/';
    private static final String USER_MESSAGE = "userMessage";
    private static final String USER_LIST = "userList";
    private static final String CHANNEL_LIST = "channelList";
    private static final String CURRENT_CHANNEL = "currentChannel";
    private static final String CANNOT_BROADCAST_MESSAGE = "Cannot broadcast message";

    private int nextUserNumber = 1;
    private Map<String, List<User>> channels = new ConcurrentHashMap<>();
    private Map<Session, User> userIdentityMap = new ConcurrentHashMap<>();
    private List<Sniffer> sniffers = Collections.synchronizedList(new LinkedList<>());

    public void addSniffer(Sniffer sniffer) {
        sniffers.add(sniffer);
    }

    public void removeSniffer(Sniffer sniffer) {
        sniffers.remove(sniffer);
    }

    public Map<String, List<User>> getChannels() {
        return channels;
    }

    public String getDefaultChannel() {
        Optional<Map.Entry<Session, User>> channel = userIdentityMap.entrySet().stream().findFirst();
        if (!channel.isPresent()) {
            channels.put(DEFAULT_CHANNEL, new LinkedList<>());
        }
        return DEFAULT_CHANNEL;
    }

    public Map<Session, User> getUsers() {
        return userIdentityMap;
    }

    private int getNextUserNumber() {
        return nextUserNumber++;
    }


    void init() {
        channels.put(DEFAULT_CHANNEL, new LinkedList<>());

        Spark.staticFileLocation(STATIC_FILE_LOCATION);
        Spark.webSocket(WEBSOCKET_PATH, new ChatWebSocketHandler(this));
        Spark.init();
    }

    public void broadcastMessage(User sender, String message) {
        List<User> userList = userIdentityMap.entrySet().stream()
                .filter(e -> e.getKey().isOpen())
                .filter(e -> e.getValue().getChannel()
                        .equals(sender.getChannel()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<String> userNames = userList.stream().map(User::getUserName).collect(Collectors.toList());

        userList.forEach(user -> {
            try {
                user.getSession().getRemote().sendString(String.valueOf(new JSONObject()
                        .put(USER_MESSAGE, createHtmlMessageFromSender(
                                sender.getUserName(), message))
                        .put(USER_LIST, userNames)
                        .put(CHANNEL_LIST, channels.keySet())
                        .put(CURRENT_CHANNEL, sender.getChannel())
                ));
            } catch (JSONException | IOException e) {
                System.err.println(CANNOT_BROADCAST_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + SAYS),
                p(message),
                span().withClass(TIMESTAMP).withText(new SimpleDateFormat(HH_MM_SS).format(new Date()))
        ).render();
    }


    public void parseCommand(Session user, String message) {
        String[] tokens = message.split(TOKEN_SEPARATOR);
        switch (tokens[0]) {
            case NEW_USER:
                if (tokens.length >= 2) {
                    newUserIdentity(user, tokens[1]);
                }
                break;
            case SWITCH_CHANNEL:
                if (tokens.length >= 2) {
                    switchChannel(user, tokens[1]);
                }
                break;
            case ADD_CHANNEL:
                if (tokens.length >= 2) {
                    addChannel(user, tokens[1]);
                }
        }
    }

    private void addChannel(Session user, String addedChannel) {
        channels.put(addedChannel, new LinkedList<>());
        switchChannel(user, addedChannel);
    }

    private void switchChannel(Session user, String targetChannel) {
        User u = userIdentityMap.get(user);

        channels.get(u.getChannel()).remove(u);
        broadcastMessage(u, u.getUserName() + LEFT_THE_CHAT);

        u.setChannel(targetChannel);
        channels.get(targetChannel).add(u);
        broadcastMessage(u, u.getUserName() + JOINED_THE_CHAT);
    }

    private void newUserIdentity(Session user, String username) {
        User userIdentity = new User(username, getDefaultChannel(), user);
        getUsers().remove(user);
        getUsers().put(user, userIdentity);
        getChannels().get(getDefaultChannel()).add(userIdentity);
        broadcastMessage(userIdentity, username + JOINED_THE_CHAT);
    }

    public void addUser(Session user) {
        String username = USER + getNextUserNumber();
        userIdentityMap.put(user, new User(username, getDefaultChannel(), user));
    }

    public void removeUser(Session user) {
        User userIdentity = userIdentityMap.get(user);

        messageFromServer(userIdentity, userIdentity.getUserName() + LEFT_THE_CHAT);

        userIdentityMap.remove(user);
    }

    public void processMessage(Session user, String message) {
        if (message.charAt(0) == COMMAND_START) {
            parseCommand(user, message.substring(1));
        } else {
            broadcastMessage(userIdentityMap.get(user), message);
        }
        sniffers.forEach(s -> s.sniffSniff(user, message));
    }

    private void messageFromServer(User originator, String message) {
        User dummy = new User(Chat.SERVER, originator.getChannel(), originator.getSession());
        broadcastMessage(dummy, message);
    }
}
