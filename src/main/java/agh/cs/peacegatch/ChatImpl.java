package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class ChatImpl implements Chat {
    private static final String DEFAULT_CHANNEL_NAME = "Chatbot";
    private static final String NEW_USER = "newUser";
    private static final String USER = "User";
    private static final String TOKEN_SEPARATOR = ":";
    private static final String STATIC_FILE_LOCATION = "/public";
    private static final String WEBSOCKET_PATH = "/chat";
    private static final String SWITCH_CHANNEL = "switchChannel";
    private static final String ADD_CHANNEL = "addChannel";
    private static final char COMMAND_START = '/';

    private int nextUserNumber = 0;
    private ConcurrentMap<Session, User> users;
    private NameManager userNameStrategy;

    private ConcurrentMap<Name, Channel> channels;
    private NameManager channelNameStrategy;


    public ChatImpl(ConcurrentMap<Session, User> users, NameManager userNameManager,
                    ConcurrentMap<Name, Channel> channels, NameManager channelNameManager) {

        this.users = users;
        this.userNameStrategy = userNameManager;
        this.channels = channels;
        this.channelNameStrategy = channelNameManager;
    }

    @Override
    public User findUserBySession(Session session) {
        return users.get(session);
    }

    @Override
    public User startSession(Session session) {
        User user = new UserImpl(new NameImpl(USER + getNextUserNumber()), null, session);
        users.put(session, user);
        return user;
    }

    private int getNextUserNumber() {
        return nextUserNumber++;
    }

    @Override
    public void endSession(Session session) {
        User user = findUserBySession(session);
        user.setChannel(null);
        users.remove(session);
    }

    @Override
    public List<Channel> getChannels() {
        return Collections.unmodifiableList(new ArrayList<>(channels.values()));
    }

    @Override
    public void addChannel(Channel channel) {
        if (channel == null || !channelNameStrategy.addName(channel.getName())) {
            return;
        }
        channels.put(channel.getName(), channel);
    }

    @Override
    public void removeChannel(Channel channel) {
        if (channel == null) {
            return;
        }
        Name channelName = channel.getName();
        channels.remove(channelName);
        channelNameStrategy.removeName(channelName);
    }

    @Override
    public Channel getDefaultChannel() {
        if (channels.isEmpty()) {
            Channel defaultChannel =
                    new ChannelImpl(
                            this,
                            new NameImpl(DEFAULT_CHANNEL_NAME),
                            Collections.synchronizedList(new LinkedList<>())
                    );
            addChannel(defaultChannel);
        }
        return channels.values().iterator().next();
    }

    @Override
    public void handleMessage(Session session, String message) {
        User user = findUserBySession(session);

        if (message.charAt(0) == COMMAND_START) {
            processCommand(user, message.substring(1));
        } else {
            user.sendMessage(message);
        }
    }

    //todo some controller? + method split
    private void processCommand(User user, String message) {
        String[] tokens = message.split(TOKEN_SEPARATOR);
        switch (tokens[0]) {
            case NEW_USER:
                if (tokens.length >= 2) {
                    introduceUser(user.getSession(), new NameImpl(tokens[1]));
                }
                break;
            case SWITCH_CHANNEL:
                if (tokens.length >= 2) {
                    Name name = new NameImpl(tokens[1]);
                    if (channels.containsKey(name)) {
                        user.setChannel(channels.get(name));
                    }
                }
                break;
            case ADD_CHANNEL:
                if (tokens.length >= 2) {
                    ChannelImpl addedChannel =
                            new ChannelImpl(
                                    this,
                                    new NameImpl(tokens[1]),
                                    Collections.synchronizedList(new LinkedList<>()));
                    addChannel(addedChannel);
                    user.setChannel(addedChannel);
                }
        }
    }

    //todo username validation
    private void introduceUser(Session session, Name userName) {
        User user = findUserBySession(session);
        user.setUserName(userName);
        user.setChannel(getDefaultChannel());
    }

    public void init() {
        Channel initialized = getDefaultChannel();

        Spark.staticFileLocation(STATIC_FILE_LOCATION);
        Spark.webSocket(WEBSOCKET_PATH, new ChatWebSocketHandler(this));
        Spark.init();
    }
}
