package agh.cs.peacegatch;

import java.util.concurrent.ConcurrentHashMap;

/**
 * PeaceGatch
 * Created by luknw on 17.01.2017
 */

public class Main {
    public static void main(String[] args) {
        ChatImpl c = new ChatImpl(new ConcurrentHashMap<>(), new UniqueNameManager(),
                new ConcurrentHashMap<>(), new UniqueNameManager());
//        c.addSniffer(new Chatbot("Cantor", c, c.getDefaultChannel()));
        c.init();
    }
}
