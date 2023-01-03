package dev.bamban.aplanes.messages.handlers;

import dev.bamban.aplanes.Message;
import dev.bamban.aplanes.MessagePublisher;
import dev.bamban.aplanes.MessageRepository;

import java.util.function.Consumer;

public class CpdlcMessageConsumer implements Consumer<Message> {
    private final MessageRepository messageRepository;
    private final MessagePublisher messagePublisher;
    public CpdlcMessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.messagePublisher = MessagePublisher.getInstance();
    }

    @Override
    public void accept(Message message) {
        messageRepository.save(message);
        messagePublisher.addMessage(message);
    }
}
