package dev.bamban.aplanes.messages.handlers;

import dev.bamban.aplanes.Message;
import dev.bamban.aplanes.MessageType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class MessageHandler implements Consumer<Message> {
    private final Map<MessageType, Consumer<Message>> handlers;

    public MessageHandler() {
        this.handlers = new HashMap<>();
    }

    public void register(MessageType messageType, Consumer<Message> messageConsumer) {
        Objects.requireNonNull(messageType);
        Objects.requireNonNull(messageConsumer);

        handlers.put(messageType, messageConsumer);
    }

    public void accept(Message message) {
        if (!handlers.containsKey(message.getType())) {
            throw new RuntimeException("Unhandled Message Type");
        }

        handlers.get(message.getType()).accept(message);
    }
}
