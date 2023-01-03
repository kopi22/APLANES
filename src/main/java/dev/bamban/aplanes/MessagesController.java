package dev.bamban.aplanes;

import dev.bamban.aplanes.messages.handlers.CpdlcMessageConsumer;
import dev.bamban.aplanes.messages.handlers.InfoReqMessageConsumer;
import dev.bamban.aplanes.messages.handlers.MessageHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/messages")
public class MessagesController {
    private static final long LONG_POLL_TIMEOUT_MS = 15_000;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final MessageRepository messageRepository;
    private final MessageHandler messageHandler;
    private final MessagePublisher messagePublisher = MessagePublisher.getInstance();


    public MessagesController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

        this.messageHandler = new MessageHandler();
        messageHandler.register(MessageType.INFOREQ, new InfoReqMessageConsumer(messageRepository));
        messageHandler.register(MessageType.CPDLC, new CpdlcMessageConsumer(messageRepository));
        messageHandler.register(MessageType.TELEX, new CpdlcMessageConsumer(messageRepository));
    }

    @GetMapping
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/poll")
    public DeferredResult<List<Message>> getNewMessages(@RequestParam(name="callsign") String receiverId) {
        DeferredResult<List<Message>> newMessages = new DeferredResult<>(LONG_POLL_TIMEOUT_MS);
        var messageSubscriber = new MessageSubscriber();
        messagePublisher.addListener(receiverId, messageSubscriber);

        var task = new FutureTask<Void>(() -> {
            try {
                while (messageSubscriber.getReceivedMessages().size() == 0) {
                    Thread.sleep(1000);
                }
                newMessages.setResult(messageSubscriber.getReceivedMessages());
            } catch (InterruptedException e) {
                newMessages.setErrorResult(List.of());
            }
            return null;
        });

        executorService.submit(task);

        try {
            task.get(LONG_POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            newMessages.setErrorResult(List.of());
        }

        return newMessages;
    }

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        messageHandler.accept(message);
        return message;
    }
}
