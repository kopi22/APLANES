package dev.bamban.aplanes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagePublisher {
    private static MessagePublisher instance;
    private final Map<String, MessageSubscriber> activeSubscribers;
    private final Map<String, List<Message>> awaitingMessages;

    private MessagePublisher() {
        activeSubscribers = new HashMap<>();
        awaitingMessages = new HashMap<>();
    }

    public static MessagePublisher getInstance() {
        if (instance == null) {
            instance = new MessagePublisher();
        }
        return instance;
    }

    public void addListener(String receiverId, MessageSubscriber subscriber) {
        if (activeSubscribers.containsKey(receiverId)) {
            throw new RuntimeException("Awaiting connection already exists!");
        }

        if (awaitingMessages.containsKey(receiverId)) {
            subscriber.receive(awaitingMessages.remove(receiverId));
            return;
        }

        activeSubscribers.put(receiverId, subscriber);
    }

    public void addMessage(Message message) {
        var receiverId = message.getReceiver();
        if (activeSubscribers.containsKey(receiverId)) {
            var receiver = activeSubscribers.get(receiverId);
            activeSubscribers.remove(receiverId);
            receiver.receive(List.of(message));
            return;
        }

        if (awaitingMessages.containsKey(receiverId)) {
            awaitingMessages.get(receiverId).add(message);
        } else {
            var messages = new ArrayList<Message>();
            messages.add(message);
            awaitingMessages.put(receiverId, messages);
        }
    }
}
