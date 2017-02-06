package agh.cs.peacegatch;

import java.util.Collection;

/**
 * PeaceGatch
 * Created by luknw on 05.02.2017
 */

public interface Channel {
    String getName();

    void addUser(User user);

    void removeUser(User user);

    Collection<User> getUsers();

    //todo implement Message and add support
    void broadcastMessage(/*Message*/ User sender, String message);
}
