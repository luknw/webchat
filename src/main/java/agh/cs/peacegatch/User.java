package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;

import java.util.List;

/*
  PeaceGatch
  Created by luknw on 05.02.2017
 */


//todo implement differential updates

/**
 * Represents single chat user
 */
public interface User {
    /**
     * Get User's name as appearing on chat
     *
     * @return Current username
     */
    Name getUserName();

    /**
     * Change User's name as appearing on chat
     *
     * @param userName new username
     */
    void setUserName(Name userName);

    /**
     * Get User's current channel
     *
     * @return User's current channel
     */
    Channel getChannel();

    /**
     * Changes User's channel and propagates the change to affected Channels
     *
     * @param channel target channel
     */
    void setChannel(Channel channel);

    /**
     * Get User's Session, by which he's uniquely identified in Chat.
     *
     * @return User's Session
     */
    Session getSession();

    /**
     * User creates a Message based on the content received from front-end,
     * presumably sending it to his current Channel
     *
     * @param content content of the Message received from front-end
     * @return Constructed Message based on specified content
     */
    Message sendMessage(String content);

    //todo clean with differential updates
    /**
     * User receives Message from his current Channel, presumably passing it to front-end
     *
     * @param message      received Message
     * @param userNames    tmp helper param
     * @param channelNames tmp helper param
     */
    void receiveMessage(Message message, List<Name> userNames, List<Name> channelNames);
}
