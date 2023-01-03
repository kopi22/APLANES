package dev.bamban.aplanes;

import java.util.ArrayList;
import java.util.List;

public class MessageSubscriber {

    private final List<Message> receivedMessages;

    public MessageSubscriber() {
        this.receivedMessages = new ArrayList<>();
    }

    void receive(List<Message> messages) {
        receivedMessages.addAll(messages);
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }
}
