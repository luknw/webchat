package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class User {
    private String userName;
    private String channelName;
    private Session session;

    public User(String userName, String channelName, Session session) {
        this.userName = userName;
        this.channelName = channelName;
        this.session = session;
    }

    public String getUserName() {
        return userName;
    }

    public String getChannelName() {
        return channelName;
    }

    public Session getSession() {
        return session;
    }
}
