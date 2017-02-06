package agh.cs.peacegatch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

/**
 * PeaceGatch
 * Created by luknw on 01.02.2017
 */

public class ChannelImpl implements Channel {
    private static final String SERVER = "Server";
    private static final String USER_MESSAGE = "userMessage";
    private static final String USER_LIST = "userList";
    private static final String CHANNEL_LIST = "channelList";
    private static final String CURRENT_CHANNEL = "currentChannel";
    private static final String CANNOT_BROADCAST_MESSAGE = "Cannot broadcast message";
    private static final String TIMESTAMP = "timestamp";
    private static final String SAYS = " says:";
    private static final String HH_MM_SS = "HH:mm:ss";

    private Chat context;

    private String name;

    private ConcurrentMap<String, User> users;

    public ChannelImpl(Chat context, String name, ConcurrentMap<String, User> users) {
        this.context = context;
        this.name = name;
        this.users = users;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addUser(User user) {
        users.put(user.getUserName(), user);
    }

    @Override
    public void removeUser(User user) {
        users.remove(user.getUserName(), user);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void broadcastMessage(User sender, String message) {
        Collection<User> userCollection = users.values();

        List<String> userNames =
                userCollection.stream()
                        .map(User::getUserName)
                        .collect(Collectors.toList());

        List<String> channelNames =
                context.getChannels().values().stream()
                        .map(Channel::getName)
                        .collect(Collectors.toList());

        userCollection.forEach(user -> {
            try {
                user.getSession().getRemote().sendString(String.valueOf(new JSONObject()
                        .put(USER_MESSAGE, createHtmlMessageFromSender(
                                sender.getUserName(), message))
                        .put(USER_LIST, userNames)
                        .put(CHANNEL_LIST, channelNames)
                        .put(CURRENT_CHANNEL, getName())
                ));
            } catch (JSONException | IOException e) {
                System.err.println(CANNOT_BROADCAST_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    public String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + SAYS),
                p(message),
                span().withClass(TIMESTAMP).withText(new SimpleDateFormat(HH_MM_SS).format(new Date()))
        ).render();
    }

    private void messageFromServer(User originator, String message) {
        User dummy = new UserImpl(SERVER, this, originator.getSession());
        broadcastMessage(dummy, message);
    }
}
