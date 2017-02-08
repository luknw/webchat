package agh.cs.peacegatch;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PeaceGatch
 * Created by luknw on 01.02.2017
 */

public class ChannelImpl implements Channel {
    private static final String SERVER = "Server";
    private static final String JOINED_CHANNEL = " joined channel";
    private static final String LEFT_CHANNEL = " left channel";

    private final User asUser = new UserImpl(new NameImpl(SERVER), this, null);

    private Chat context;
    private Name name;
    private List<User> users;

    public ChannelImpl(Chat context, Name name, List<User> users) {
        this.context = context;
        this.name = name;
        this.users = users;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        messageFromServer(user.getUserName() + JOINED_CHANNEL);
    }

    @Override
    public void removeUser(User user) {
        users.remove(user);
        messageFromServer(user.getUserName() + LEFT_CHANNEL);
    }

    @Override
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    private void messageFromServer(String content) {
        Message message = new MessageImpl(asUser, content);
        broadcastMessage(message);
    }

    //todo differential updates
    @Override
    public void broadcastMessage(Message message) {
        List<Name> userNames =
                getUsers().stream()
                        .map(User::getUserName)
                        .collect(Collectors.toList());

        List<Name> channelNames =
                context.getChannels().stream()
                        .map(Channel::getName)
                        .collect(Collectors.toList());

        getUsers().forEach(user -> user.receiveMessage(message, userNames, channelNames));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelImpl other = (ChannelImpl) o;

        return (context != null ? context.equals(other.context) : other.context == null)
                && (name != null ? name.equals(other.name) : other.name == null);
    }

    @Override
    public int hashCode() {
        int result = context != null ? context.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
