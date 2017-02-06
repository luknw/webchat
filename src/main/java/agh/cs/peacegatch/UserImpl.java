package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class UserImpl implements User {
    private String userName;
    private Channel channel;
    private Session session;

    public UserImpl(String userName, Channel channel, Session session) {
        this.userName = userName;
        this.channel = channel;
        this.session = session;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel targetChannel) {
        if (targetChannel != null) {
            targetChannel.addUser(this);
            if (getChannel() != null) {
                getChannel().removeUser(this);
            }
        }
        this.channel = targetChannel;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserImpl user = (UserImpl) o;

        return (userName != null ? userName.equals(user.userName) : user.userName == null)
                && (channel != null ? channel.equals(user.channel) : user.channel == null)
                && (session != null ? session.equals(user.session) : user.session == null);
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        result = 31 * result + (session != null ? session.hashCode() : 0);
        return result;
    }
}
