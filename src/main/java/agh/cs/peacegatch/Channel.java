package agh.cs.peacegatch;

import java.util.List;

/*
  PeaceGatch
  Created by luknw on 05.02.2017
 */

//todo implement differential updates

/**
 * Enables messaging between Users connected to this channel
 */
public interface Channel {
    /**
     * @return Channel name
     */
    Name getName();

    /**
     * Adds user to the Channel. May send a message to the Channel.
     * Works only as an internal bookkeeping operation,
     * no changes are made outside of the affected Channel instance.
     * If the User already is in the Channel, nothing happens.
     *
     * @param user user to add
     */
    void addUser(User user);

    /**
     * Removes user from the Channel. May send a message to the Channel.
     * Works only as an internal bookkeeping operation,
     * no changes are made outside of the affected Channel instance.
     * If the User isn't in the Chanel, nothing happens.
     *
     * @param user user to remove
     */
    void removeUser(User user);

    /**
     * @return Unmodifiable List of Users in the Channel
     */
    List<User> getUsers();

    /**
     * Sends message to all Users, which are in this Channel.
     * Generally, {@code receiveMessage(message)} should be called on each User in this Channel.
     *
     * @param message message to be sent
     */
    void broadcastMessage(Message message);
}
