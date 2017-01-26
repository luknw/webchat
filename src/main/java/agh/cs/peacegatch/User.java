package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class User {
    private String userName;
    private String channel;
    private Session session;

    public User(String userName, String channel, Session session) {
        this.userName = userName;
        this.channel = channel;
        this.session = session;
    }

    public String getUserName() {
        return userName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Session getSession() {
        return session;
    }
}
