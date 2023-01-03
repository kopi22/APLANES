package dev.bamban.aplanes.messages.processors;

import dev.bamban.aplanes.Message;

public interface MessageProcessor {
    void process(Message message);
}
