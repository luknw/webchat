package agh.cs.peacegatch;

import org.eclipse.jetty.websocket.api.Session;

/**
 * PeaceGatch
 * Created by luknw on 26.01.2017
 */

public interface Sniffer {
    void sniffSniff(Session user, String message);
}
