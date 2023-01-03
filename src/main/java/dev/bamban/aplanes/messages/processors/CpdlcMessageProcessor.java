package dev.bamban.aplanes.messages.processors;

import dev.bamban.aplanes.Message;
import dev.bamban.aplanes.MessagePublisher;
import dev.bamban.aplanes.MessageRepository;

public class CpdlcMessageProcessor implements MessageProcessor {
    private final MessageRepository messageRepository;
    private final MessagePublisher messagePublisher;
    public CpdlcMessageProcessor(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.messagePublisher = MessagePublisher.getInstance();
    }

    @Override
    public void process(Message message) {
        messageRepository.save(message);
        messagePublisher.addMessage(message);
    }
}
