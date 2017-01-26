package agh.cs.peacegatch;

/**
 * PeaceGatch
 * Created by luknw on 17.01.2017
 */

public class Main {
    public static void main(String[] args) {
        Chat c = new Chat();
        c.addSniffer(new Chatbot("Cantor", c, c.getDefaultChannel()));
        c.init();
    }
}
