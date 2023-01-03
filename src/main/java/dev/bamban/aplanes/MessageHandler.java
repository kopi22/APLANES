package dev.bamban.aplanes;

import dev.bamban.aplanes.messages.processors.MessageProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageHandler implements MessageProcessor {
    private final Map<MessageType, MessageProcessor> handlers;

    public MessageHandler() {
        this.handlers = new HashMap<>();
    }

    public void register(MessageType messageType, MessageProcessor messageProcessor) {
        Objects.requireNonNull(messageType);
        Objects.requireNonNull(messageProcessor);

        handlers.put(messageType, messageProcessor);
    }

    public void process(Message message) {
        if (!handlers.containsKey(message.getType())) {
            throw new RuntimeException("Unhandled Message Type");
        }

        handlers.get(message.getType()).process(message);
    }
}
