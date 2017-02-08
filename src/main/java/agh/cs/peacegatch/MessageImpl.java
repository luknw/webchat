package agh.cs.peacegatch;

/**
 * PeaceGatch
 * Created by luknw on 08.02.2017
 */

public class MessageImpl implements Message {
    private final User sender;
    private final String content;

    public MessageImpl(User sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public String getContent() {
        return content;
    }
}
