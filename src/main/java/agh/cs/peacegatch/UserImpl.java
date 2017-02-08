package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

/**
 * PeaceGatch
 * Created by luknw on 25.01.2017
 */

public class UserImpl implements User {
    private static final String USER_MESSAGE = "userMessage";
    private static final String USER_LIST = "userList";
    private static final String CHANNEL_LIST = "channelList";
    private static final String CURRENT_CHANNEL = "currentChannel";
    private static final String CANNOT_BROADCAST_MESSAGE = "Cannot broadcast message";
    private static final String TIMESTAMP = "timestamp";
    private static final String SAYS = " says:";
    private static final String HH_MM_SS = "HH:mm:ss";

    private Name userName;
    private Channel channel;
    private Session session;

    public UserImpl(Name userName, Channel channel, Session session) {
        this.userName = userName;
        this.channel = channel;
        this.session = session;
    }

    @Override
    public Name getUserName() {
        return userName;
    }

    @Override
    public void setUserName(Name userName) {
        this.userName = userName;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel targetChannel) {
        if (getChannel() == targetChannel) {
            return;
        }

        if (getChannel() != null) {
            getChannel().removeUser(this);
        }
        this.channel = targetChannel;
        if (targetChannel != null) {
            targetChannel.addUser(this);
        }
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public Message sendMessage(String content) {
        Message message = new MessageImpl(this, content);
        getChannel().broadcastMessage(message);
        return message;
    }

    //todo differential updates
    @Override
    public void receiveMessage(Message message, List<Name> userNames, List<Name> channelNames) {
        try {
            getSession().getRemote().sendString(String.valueOf(new JSONObject()
                    .put(USER_MESSAGE, createHtmlMessageFromSender(
                            message.getSender().getUserName().toString(), message.getContent()))
                    .put(USER_LIST, userNames.stream().map(Name::toString).collect(Collectors.toList()))
                    .put(CHANNEL_LIST, channelNames.stream().map(Name::toString).collect(Collectors.toList()))
                    .put(CURRENT_CHANNEL, getChannel().getName().toString())
            ));
        } catch (JSONException | IOException e) {
            System.err.println(CANNOT_BROADCAST_MESSAGE);
            e.printStackTrace();
        }
    }

    //todo create proper class for Message formatting
    private String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + SAYS),
                p(message),
                span().withClass(TIMESTAMP).withText(new SimpleDateFormat(HH_MM_SS).format(new Date()))
        ).render();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserImpl other = (UserImpl) o;

        return session != null ? session.equals(other.session) : other.session == null;
    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }
}
