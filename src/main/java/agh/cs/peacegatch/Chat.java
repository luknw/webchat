package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;

import java.util.List;

/*
  PeaceGatch
  Created by luknw on 07.02.2017
 */

//todo implement differential updates

/**
 * Manages Channels and user Sessions
 */
public interface Chat {
    /**
     * Provides the way to find User represented by the specified session
     *
     * @param session session to find user for
     * @return User represented by the specified session
     */
    User findUserBySession(Session session);

    /**
     * Initiates User's connection, assigning him arbitrarily chosen defaults
     *
     * @param session Session associated with the new User
     */
    User startSession(Session session);

    /**
     * Ends User connection associated with the specified session
     *
     * @param session session to end
     */
    void endSession(Session session);

    /**
     * @return Unmodifiable List of channels managed by this Chat
     */
    List<Channel> getChannels();

    /**
     * Adds the specified channel to the Chat.
     * If the Chat already has this channel, nothing happens.
     *
     * @param channel channel to add
     */
    void addChannel(Channel channel);

    /**
     * Removes the specified channel from the Chat.
     * If the Chat doesn't have this channel, nothing happens.
     *
     * @param channel channel to remove
     */
    void removeChannel(Channel channel);

    /**
     * @return Default channel for new users
     */
    Channel getDefaultChannel();

    //todo Message format and handling
    /**
     * Temporary message handling
     *
     * @param session Session of the User sending message
     * @param message raw user message
     */
    void handleMessage(Session session, String message);
}
