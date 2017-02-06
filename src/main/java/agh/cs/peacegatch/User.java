package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;

/**
 * PeaceGatch
 * Created by luknw on 05.02.2017
 */

public interface User {
    String getUserName();

    Channel getChannel();

    void setChannel(Channel channel);

    Session getSession();

//    //todo Message support
//    void associateMessage(String message);
//
//    //todo Message support
//    void notifyMessage(String message);
}
