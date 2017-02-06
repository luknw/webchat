package agh.cs.peacegatch;

import java.util.concurrent.ConcurrentHashMap;

/**
 * PeaceGatch
 * Created by luknw on 17.01.2017
 */

public class Main {
    public static void main(String[] args) {
        Chat c = new Chat(new ConcurrentHashMap<>(), new UniqueNameStrategy(),
                new ConcurrentHashMap<>(), new UniqueNameStrategy());
//        c.addSniffer(new Chatbot("Cantor", c, c.getDefaultChannel()));
        c.init();
    }
}
