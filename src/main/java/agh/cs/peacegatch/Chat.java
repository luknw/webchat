package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class Chat {
    private static final String DEFAULT_CHANNEL_NAME = "Chatbot";
    private static final String NEW_USER = "newUser";
    private static final String USER = "User";
    private static final String TOKEN_SEPARATOR = ":";
    private static final String STATIC_FILE_LOCATION = "/public";
    private static final String WEBSOCKET_PATH = "/chat";
    private static final String JOINED_CHANNEL = " joined channel";
    private static final String LEFT_CHANNEL = " left channel";
    private static final String SWITCH_CHANNEL = "switchChannel";
    private static final String ADD_CHANNEL = "addChannel";
    private static final char COMMAND_START = '/';

    private int nextUserNumber = 0;
    private ConcurrentMap<Session, User> userMap = new ConcurrentHashMap<>();
    private NameManagementStrategy userNameStrategy;

    private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();
    private NameManagementStrategy channelNameStrategy;


    public Chat(ConcurrentMap<Session, User> userMap, NameManagementStrategy userNameStrategy,
                ConcurrentMap<String, Channel> channels, NameManagementStrategy channelNameStrategy) {

        this.userMap = userMap;
        this.userNameStrategy = userNameStrategy;
        this.channels = channels;
        this.channelNameStrategy = channelNameStrategy;
    }

    public Map<Session, User> getUsers() {
        return userMap;
    }

    public Map<String, Channel> getChannels() {
        return channels;
    }

    public Channel getDefaultChannel() {
        return getChannels().values().stream()
                .findFirst()
                .orElse(new ChannelImpl(this, DEFAULT_CHANNEL_NAME, new ConcurrentHashMap<>()));
    }

    void init() {
        if (channels.isEmpty()) {
            Channel defaultChannel = getDefaultChannel();
            channels.put(defaultChannel.getName(), defaultChannel);
        }

        Spark.staticFileLocation(STATIC_FILE_LOCATION);
        Spark.webSocket(WEBSOCKET_PATH, new ChatWebSocketHandler(this));
        Spark.init();
    }

    private int getNextUserNumber() {
        return nextUserNumber++;
    }

    public void processCommand(User user, String message) {
        String[] tokens = message.split(TOKEN_SEPARATOR);
        switch (tokens[0]) {
            case NEW_USER:
                if (tokens.length >= 2) {
                    introduceUser(user.getSession(), tokens[1]);
                }
                break;
            case SWITCH_CHANNEL:
                if (tokens.length >= 2) {
                    moveUserToChannel(user, getChannels().get(tokens[1]));
                }
                break;
            case ADD_CHANNEL:
                if (tokens.length >= 2) {
                    addChannel(user, new ChannelImpl(this, tokens[1], new ConcurrentHashMap<>()));
                }
        }
    }

    private void addChannel(User user, Channel channel) {
        channels.put(channel.getName(), channel);
        moveUserToChannel(user, channel);
    }

    private void moveUserToChannel(User user, Channel targetChannel) {
        Channel oldChannel = user.getChannel();

        user.setChannel(targetChannel);

        if (oldChannel != null) {
            oldChannel.broadcastMessage(user, user.getUserName() + LEFT_CHANNEL);
        }
        if (targetChannel != null) {
            targetChannel.broadcastMessage(user, user.getUserName() + JOINED_CHANNEL);
        }
    }

    private void introduceUser(Session session, String userName) {
        User user = new UserImpl(userName, null, session);
        getUsers().put(session, user);
        moveUserToChannel(user, getDefaultChannel());
    }

    public void startSession(Session session) {
        User user = new UserImpl(USER + getNextUserNumber(), null, session);
        userMap.put(session, user);
    }

    public void endSession(Session session) {
        User user = getUsers().get(session);
        user.setChannel(null);
        userMap.remove(session);
    }

    public void processMessage(Session session, String message) {
        User user = getUsers().get(session);

        if (message.charAt(0) == COMMAND_START) {
            processCommand(user, message.substring(1));
        } else {
            user.getChannel().broadcastMessage(user, message);
        }
    }
}
